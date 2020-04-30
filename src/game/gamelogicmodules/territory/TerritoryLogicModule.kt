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

        fun getTerritoryLogicModule(game: Game): TerritoryLogicModule {
            return game.moduleOfType(type) as TerritoryLogicModule
        }

        private fun territoriesFromSaveString(saveString: Map<String, Any>): Collection<Territory>{
            return (saveString[TERRITORIES_NAME] as List<Map<String, Any>>).map{map -> Territory(map)}
        }
    }

    override val type = Companion.type


    val territories: Collection<Territory>

    constructor(territories: Collection<Territory>): super(territories.map{AgricultureReportFactory(it)}) {
        this.territories = territories
    }

    constructor(other: TerritoryLogicModule): super(other.territories.map{AgricultureReportFactory(it)}){
        this.territories = other.territories.map { Territory(it) }
    }

    constructor(saveString: Map<String, Any>, game: Game): super(territoriesFromSaveString(saveString).map{AgricultureReportFactory(it)}){
        this.territories = territoriesFromSaveString(saveString)
    }

    override fun endTurn(): List<Effect> {
        return listOf()
    }

    override fun value(perspective: GameCharacter): Double {
        return 0.0
    }

    override fun specialSaveString(): Map<String, Any> {
        return mapOf(
            TERRITORIES_NAME to territories.map { it.saveString() }
        )
    }

    fun matchingTerritory(territory: Territory): Territory{
        return territories.filter { it == territory }.first()
    }

    fun territoryById(id: Int): Territory{
        return territories.filter { it.id == id }.first()
    }
}