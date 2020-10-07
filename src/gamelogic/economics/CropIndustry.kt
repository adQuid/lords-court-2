package gamelogic.economics

import game.Game
import gamelogic.economics.EconomicsLogicModule.Companion.LABOR_NAME
import gamelogic.resources.ResourceTypes
import gamelogic.territory.Crop
import gamelogic.territory.Territory
import gamelogic.territory.TerritoryLogicModule
import kotlin.math.min

object CropIndustry: Industry {
    override fun run(territory: Territory, game: Game, labor: Int): Int {
        val territoryLogic = game.moduleOfType(TerritoryLogicModule.type) as TerritoryLogicModule
        val economicsLogic = game.moduleOfType(EconomicsLogicModule.type) as EconomicsLogicModule

        var laborLeft = labor
        var landLeft = territory.resources.get(ResourceTypes.ARABLE_LAND_NAME) - territory.totalCropsPlanted()
        if(!territoryLogic.isGrowingSeason()){
            territory.crops.forEach { it.quantity /= 4 }
        }
        //people harvest crops
        val thisHarvest = mutableListOf<Crop>()
        territory.crops.forEach {
                crop -> if(crop.readyToHarvest(game)){
            val toHarvest = min(laborLeft, crop.quantity)
            if(toHarvest > 0){
                thisHarvest.add(Crop(toHarvest, crop.plantingTime))
                territory.modifyResource(ResourceTypes.SEEDS_NAME, crop.yield() * toHarvest)
                laborLeft -= toHarvest
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
        val toPlant = min(min(landLeft, laborLeft), territory.resources.get(ResourceTypes.SEEDS_NAME))
        if(toPlant > 0){
            val cropToPlant = Crop(toPlant, game.turn)
            if(territoryLogic.goodIdeaToPlant(cropToPlant)){
                territory.modifyResource(ResourceTypes.SEEDS_NAME, -toPlant)
                territory.crops.add(cropToPlant)
                laborLeft -= toPlant
            }
        }

        //milling seeds
        val seedsToSave = territory.resources.get(ResourceTypes.ARABLE_LAND_NAME) * 1.2
        var toMill = territory.resources.get(ResourceTypes.SEEDS_NAME) - seedsToSave
        if(toMill > 0){
            var structureToUse = economicsLogic.bestConversion(territory, listOf(LABOR_NAME), ResourceTypes.FLOUR_NAME)
            while(structureToUse != null){
                val manufacture = structureToUse.bestManufactureOption(listOf(LABOR_NAME), ResourceTypes.FLOUR_NAME)!!
                val quantToMill = min(toMill.toInt()/4, min(laborLeft, manufacture.thruput - structureToUse.usesExpended))
                territory.resources.addAll(structureToUse!!.runManufacture(manufacture, quantToMill))

                if(quantToMill > 0 && toMill > 0){
                    structureToUse = economicsLogic.bestConversion(territory, listOf(LABOR_NAME), ResourceTypes.FLOUR_NAME)
                } else {
                    structureToUse = null
                }
            }
            toMill = territory.resources.get(ResourceTypes.SEEDS_NAME) - seedsToSave
            territory.modifyResource(ResourceTypes.SEEDS_NAME, -toMill.toInt())
            territory.modifyResource(ResourceTypes.FLOUR_NAME, toMill.toInt())
            laborLeft -= toMill.toInt()
        }

        //bake just enough bread to eat
        val breadToMake = min((territory.resources.get(ResourceTypes.POPULATION_NAME)-territory.resources.get(
            ResourceTypes.BREAD_NAME))/2, territory.resources.get(
            ResourceTypes.FLOUR_NAME))
        if(breadToMake > 0){
            territory.modifyResource(ResourceTypes.BREAD_NAME, breadToMake*2)
            territory.modifyResource(ResourceTypes.FLOUR_NAME, -breadToMake)
        }

        //eat food
        //TODO: Will I never need this step?
        val foodToEat = territory.foodToEatNextTurn()
        val totalFood = foodToEat.resources.entries.sumBy { it.value }
        if(totalFood >= territory.foodNeededNextTurn()){
            territory.resources.subtractAll(foodToEat)
        } else {
            //println("starvation")
            territory.resources.multiply(
                ResourceTypes.POPULATION_NAME, (1.0 + (totalFood / territory.resources.get(
                    ResourceTypes.POPULATION_NAME)))/2.0)
            territory.resources.set(ResourceTypes.BREAD_NAME, 0)
        }

        return laborLeft
    }
}