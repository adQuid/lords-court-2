package gamelogic.economics

import aibrain.Plan
import aibrain.Score
import game.Game
import game.GameCharacter
import game.GameLogicModule
import game.Location
import gamelogic.resources.ResourceTypes
import gamelogic.territory.Crop
import gamelogic.territory.Territory
import gamelogic.territory.TerritoryLogicModule
import kotlin.math.min

class EconomicsLogicModule: GameLogicModule {
    companion object {
        val type = "Economics"


    }

    override val type = EconomicsLogicModule.type

    constructor(): super(listOf(), EconomicsTitleFactory(), listOf(TerritoryLogicModule.type)){

    }

    constructor(other: EconomicsLogicModule): super(listOf(), EconomicsTitleFactory(), listOf(TerritoryLogicModule.type)){

    }

    constructor(saveString: Map<String, Any>, game: Game): super(listOf(), EconomicsTitleFactory(), listOf(TerritoryLogicModule.type)){

    }

    override fun finishConstruction(game: Game) {
        //do nothing
    }

    override fun endTurn(game: Game) {

        val territoryLogic = game.moduleOfType(TerritoryLogicModule.type) as TerritoryLogicModule

        territoryLogic.territories().forEach { growCrops(it, game) }

    }

    private fun growCrops(territory: Territory, game: Game){
        val territoryLogic = game.moduleOfType(TerritoryLogicModule.type) as TerritoryLogicModule

        var farmersLeft = territory.resources.get(ResourceTypes.POPULATION_NAME)
        var landLeft = territory.resources.get(ResourceTypes.ARABLE_LAND_NAME) - territory.totalCropsPlanted()
        if(!territoryLogic.isGrowingSeason()){
            territory.crops.forEach { it.quantity /= 4 }
        }
        //people harvest crops
        val thisHarvest = mutableListOf<Crop>()
        territory.crops.forEach {
                crop -> if(crop.readyToHarvest(game)){
            val toHarvest = min(farmersLeft, crop.quantity)
            if(toHarvest > 0){
                thisHarvest.add(Crop(toHarvest, crop.plantingTime))
                territory.modifyResource(ResourceTypes.SEEDS_NAME, crop.yield() * toHarvest)
                farmersLeft -= toHarvest
                crop.quantity -= toHarvest
            } else if(game.isLive){
                println("a crop is ready, but not being harvested")
            }
            //TODO: Should crops rot this quickly?
            crop.quantity /= 2
        }
        }
        territory.lastHarvest = thisHarvest
        territory.crops.removeIf { it.quantity < 1 }

        //people plant
        val toPlant = min(min(landLeft, farmersLeft), territory.resources.get(ResourceTypes.SEEDS_NAME))
        if(toPlant > 0){
            val cropToPlant = Crop(toPlant, game.turn)
            if(territoryLogic.goodIdeaToPlant(cropToPlant)){
                territory.modifyResource(ResourceTypes.SEEDS_NAME, -toPlant)
                territory.crops.add(cropToPlant)
                farmersLeft -= toPlant
            }
        }

        //milling seeds
        val seedsToSave = territory.resources.get(ResourceTypes.ARABLE_LAND_NAME) * 1.2
        val toMill = territory.resources.get(ResourceTypes.SEEDS_NAME) - seedsToSave
        if(toMill > 0){
            territory.modifyResource(ResourceTypes.SEEDS_NAME, -toMill.toInt())
            territory.modifyResource(ResourceTypes.FLOUR_NAME, toMill.toInt())
        }

        //bake just enough bread to eat
        val breadToMake = min((territory.resources.get(ResourceTypes.POPULATION_NAME)-territory.resources.get(ResourceTypes.BREAD_NAME))/2, territory.resources.get(
            ResourceTypes.FLOUR_NAME))
        if(breadToMake > 0){
            territory.modifyResource(ResourceTypes.BREAD_NAME, breadToMake*2)
            territory.modifyResource(ResourceTypes.FLOUR_NAME, -breadToMake)
        }

        //eat food
        //TODO: Will I never need this step?
        val foodToEat = territory.foodToEatNextTurn()
        val totalFood = foodToEat.resources.entries.sumBy { it.value }
        if(totalFood >= territory.resources.get(ResourceTypes.POPULATION_NAME)){
            territory.resources.subtractAll(foodToEat)
        } else {
            //println("starvation")
            territory.resources.multiply(
                ResourceTypes.POPULATION_NAME, (1.0 + (totalFood / territory.resources.get(
                    ResourceTypes.POPULATION_NAME)))/2.0)
            territory.resources.set(ResourceTypes.BREAD_NAME, 0)
        }

    }

    override fun locations(): Collection<Location> {
        return listOf()
    }

    override fun specialSaveString(): Map<String, Any> {
        return mapOf()
    }

    override fun planOptions(
        perspective: GameCharacter,
        importantPlayers: Collection<GameCharacter>
    ): Collection<Plan> {
        return listOf()
    }

    override fun score(perspective: GameCharacter): Score {
        return Score()
    }
}