package gamelogic.territory

import aibrain.Plan
import aibrain.Score
import game.*
import javafx.scene.control.Button
import shortstate.ShortStateCharacter
import shortstate.report.ReportFactory
import kotlin.math.min

class TerritoryLogicModule: GameLogicModule {

    companion object{
        val type = "Territory"

        val TERRITORIES_NAME = "territories"
        val WEEK_NAME = "weekofyear"
        val WEEKS_IN_YEAR = 52
        val WEEKS_PER_TURN = 2

        fun getTerritoryLogicModule(game: Game): TerritoryLogicModule {
            return game.moduleOfType(type) as TerritoryLogicModule
        }

        private fun territoriesFromSaveString(saveString: Map<String, Any>): Collection<Territory>{
            return (saveString[TERRITORIES_NAME] as List<Map<String, Any>>).map{map -> Territory(map)}
        }

        private fun reportFactories(territories: Collection<Int>): List<ReportFactory>{
            return territories.map{FoodStocksReportFactory(it)} + territories.map{ActiveCropsReportFactory(it)} + territories.map { PopulationReportFactory(it) }
        }


    }

    override val type = Companion.type


    val map: TerritoryMap
    var weekOfYear: Int

    //likely to get removed in time
    constructor(territories: TerritoryMap): super(reportFactories(territories.territories.map { it.id }), TerritoryTitleFactory, listOf()) {
        weekOfYear = 6
        this.map = territories
    }

    constructor(other: TerritoryLogicModule): super(reportFactories(other.map.territories.map{it.id}), TerritoryTitleFactory, listOf()){
        this.weekOfYear = other.weekOfYear
        this.map = TerritoryMap(other.map)
    }

    //used when making a new game from an existing map
    constructor(saveString: Map<String, Any>): super(listOf(), TerritoryTitleFactory, listOf()){
        weekOfYear = 6
        this.map = TerritoryMap(saveString[TERRITORIES_NAME] as Map<String, Any>)
    }

    constructor(saveString: Map<String, Any>, game: Game): super(listOf(), TerritoryTitleFactory, listOf()){
        this.weekOfYear = saveString[WEEK_NAME] as Int
        this.map = TerritoryMap(saveString[TERRITORIES_NAME] as Map<String, Any>)
    }

    override fun finishConstruction(game: Game) {
        reportTypes = reportFactories(map.territories.map { it.id }).associate { it.type to it }
    }

    override fun locations(): Collection<Location> {
        return listOf()
    }

    override fun endTurn(game: Game){
        weekOfYear = (weekOfYear + WEEKS_PER_TURN) % WEEKS_IN_YEAR
    }

    override fun score(perspective: GameCharacter): Score {
        return Score()
    }

    override fun planOptions(
        perspective: GameCharacter,
        importantPlayers: Collection<GameCharacter>
    ): Collection<Plan> {
        return listOf()
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

    fun goodIdeaToPlant(crop: Crop): Boolean{
        return isGrowingSeason() && willBeGrowingSeason(weekOfYear + crop.harvestAge() * WEEKS_PER_TURN)
    }

    fun isGrowingSeason(): Boolean{
        return willBeGrowingSeason(weekOfYear)
    }

    private fun willBeGrowingSeason(week: Int): Boolean{
        return week in 7..49
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
        if(week in 22..28){
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