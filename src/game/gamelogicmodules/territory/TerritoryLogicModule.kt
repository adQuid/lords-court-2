package game.gamelogicmodules.territory

import game.Effect
import game.Game
import game.GameCharacter
import game.GameLogicModule
import shortstate.report.ReportFactory
import kotlin.math.min

class TerritoryLogicModule: GameLogicModule {

    companion object{
        val type = "Territory"

        val TERRITORIES_NAME = "territories"
        val WEEK_NAME = "weekofyear"
        val WEEKS_IN_YEAR = 52

        fun getTerritoryLogicModule(game: Game): TerritoryLogicModule {
            return game.moduleOfType(type) as TerritoryLogicModule
        }

        private fun territoriesFromSaveString(saveString: Map<String, Any>): Collection<Territory>{
            return (saveString[TERRITORIES_NAME] as List<Map<String, Any>>).map{map -> Territory(map)}
        }

        private fun reportFactories(territories: Collection<Int>): List<ReportFactory>{
            return territories.map{FoodStocksReportFactory(it)} + territories.map{ActiveCropsReportFactory(it)}
        }
    }

    override val type = Companion.type


    val map: TerritoryMap
    var weekOfYear: Int

    constructor(mapName: String, territories: List<Territory>): super(reportFactories(territories.map { it.id }), listOf()) {
        weekOfYear = 6
        this.map = TerritoryMap(mapName, territories)
    }

    constructor(other: TerritoryLogicModule): super(reportFactories(other.map.territories.map{it.id}), listOf()){
        this.weekOfYear = other.weekOfYear
        this.map = TerritoryMap(other.map)
    }

    constructor(saveString: Map<String, Any>, game: Game): super(listOf(), listOf()){
        this.weekOfYear = saveString[WEEK_NAME] as Int
        this.map = TerritoryMap(saveString[TERRITORIES_NAME] as Map<String, Any>)
    }

    override fun finishConstruction(game: Game) {
        reportTypes = reportFactories(map.territories.map { it.id }).associate { it.type to it }
    }

    override fun endTurn(game: Game): List<Effect> {
        val retval = mutableListOf<Effect>()

        map.territories.forEach {
            growCrops(it)
        }

        weekOfYear = (weekOfYear + 2) % WEEKS_IN_YEAR
        return retval
    }

    override fun value(perspective: GameCharacter): Double {
        return 0.0
    }

    override fun specialSaveString(): Map<String, Any> {
        return mapOf(
            WEEK_NAME to weekOfYear,
            TERRITORIES_NAME to map.saveString()
        )
    }

    fun territories(): Collection<Territory>{
        return map.territories
    }

    fun matchingTerritory(territory: Territory): Territory{
        return map.territories.filter { it == territory }.first()
    }

    fun territoryById(id: Int): Territory{
        return map.territories.filter { it.id == id }.first()
    }

    private fun growCrops(territory: Territory){
        var farmersLeft = territory.resources.get(Territory.POPULATION_NAME)
        var landLeft = territory.resources.get(Territory.ARABLE_LAND_NAME) - territory.totalCropsPlanted()
        if(isGrowingSeason()){
            //people harvest crops
            val thisHarvest = mutableListOf<Crop>()
            territory.crops.forEach {
                crop -> if(weekOfYear - crop.plantingTime > 15){
                    val toHarvest = min(farmersLeft, crop.quantity)
                    if(toHarvest > 0){
                        thisHarvest.add(Crop(toHarvest, crop.plantingTime))
                        territory.modifyResource(Territory.SEEDS_NAME, crop.yield() * toHarvest)
                        farmersLeft -= toHarvest
                        crop.quantity -= toHarvest
                    }
                    crop.quantity /= 2
                }
            }
            territory.lastHarvest = thisHarvest
            territory.crops.removeIf { it.quantity < 1 }
            
            //people plant
            val toPlant = min(min(landLeft, farmersLeft), territory.resources.get(Territory.SEEDS_NAME))
            if(toPlant > 0){
                territory.crops.add(Crop(toPlant, weekOfYear))
                territory.modifyResource(Territory.SEEDS_NAME, -toPlant)
                farmersLeft -= toPlant
            }
        }
        
        //milling seeds
        val seedsToSave = territory.resources.get(Territory.ARABLE_LAND_NAME) * 1.2
        val toMill = territory.resources.get(Territory.SEEDS_NAME) - seedsToSave
        if(toMill > 0){
            territory.modifyResource(Territory.SEEDS_NAME, -toMill.toInt())
            territory.modifyResource(Territory.FLOUR_NAME, toMill.toInt())
        }

        //bake just enough bread to eat
        val breadToMake = min(territory.resources.get(Territory.POPULATION_NAME), territory.resources.get(Territory.FLOUR_NAME))
        if(breadToMake > 0){
            territory.modifyResource(Territory.BREAD_NAME, breadToMake*2)
            territory.modifyResource(Territory.FLOUR_NAME, -breadToMake)
        }

        //eat bread
        //TODO: Will I never need this step?
        val breadToEat = territory.resources.get(Territory.BREAD_NAME)
        if(breadToEat >= territory.resources.get(Territory.POPULATION_NAME)){
            territory.modifyResource(Territory.BREAD_NAME, -breadToEat)
        } else {
            territory.resources.set(Territory.BREAD_NAME, 0)
        }
    }

    private fun isGrowingSeason(): Boolean{
        return weekOfYear in 7..49
    }

    fun currentWeekName(): String{
        return weekName(weekOfYear)
    }

    fun weekName(week: Int): String{
        if(week in 0..4){
            return "Deep Winter"
        }
        if(week in 5..7){
            return "Snow Melt"
        }
        if(week in 8..17){
            return "Planting Season"
        }
        if(week in 18..21){
            return "Blooming Season"
        }
        if(week in 23..28){
            return "Summer"
        }
        if(week in 29..32){
            return "Harvest Season"
        }
        if(week in 33..41){
            return "Fall"
        }
        if(week in 42..48){
            return "First Frost"
        }
        return "Deep Winter"
    }
}