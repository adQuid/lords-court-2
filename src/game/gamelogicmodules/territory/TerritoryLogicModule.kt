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

        fun getTerritoryLogicModule(game: Game): TerritoryLogicModule {
            return game.moduleOfType(type) as TerritoryLogicModule
        }

        val reports = mapOf<String, ReportFactory>( "AgricultureReport" to AgricultureReportFactory())
    }

    override val type = Companion.type

    val territories = listOf(Territory())

    constructor(): super(reports) {
    }

    constructor(other: TerritoryLogicModule): super(reports){
    }

    constructor(saveString: Map<String, Any>, game: Game): super(reports){
    }

    override fun endTurn(): List<Effect> {
        return listOf()
    }

    override fun value(perspective: GameCharacter): Double {
        return 0.0
    }

    override fun specialSaveString(): Map<String, Any> {
        return mapOf(

        )
    }
}