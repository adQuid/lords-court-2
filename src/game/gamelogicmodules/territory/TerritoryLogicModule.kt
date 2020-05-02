package game.gamelogicmodules.territory

import game.Effect
import game.Game
import game.GameCharacter
import game.GameLogicModule
import shortstate.report.Report
import shortstate.report.ReportFactory

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
    }

    override val type = Companion.type


    val territories: Collection<Territory>
    var weekOfYear: Int

    constructor(territories: Collection<Territory>): super(territories.map{AgricultureReportFactory(it)}) {
        weekOfYear = 0
        this.territories = territories
    }

    constructor(other: TerritoryLogicModule): super(other.territories.map{AgricultureReportFactory(it)}){
        this.weekOfYear = other.weekOfYear
        this.territories = other.territories.map { Territory(it) }
    }

    constructor(saveString: Map<String, Any>, game: Game): super(territoriesFromSaveString(saveString).map{AgricultureReportFactory(it)}){
        this.weekOfYear = saveString[WEEK_NAME] as Int
        this.territories = territoriesFromSaveString(saveString)
    }

    override fun endTurn(): List<Effect> {
        val retval = mutableListOf<Effect>()

        territories.forEach {
            if(isGrowingSeason()){
                it.modifyResource(it.WHEAT_NAME, 1)
            }
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
            TERRITORIES_NAME to territories.map { it.saveString() }
        )
    }

    fun matchingTerritory(territory: Territory): Territory{
        return territories.filter { it == territory }.first()
    }

    fun territoryById(id: Int): Territory{
        return territories.filter { it.id == id }.first()
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