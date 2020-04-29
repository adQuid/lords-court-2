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

        val reports = listOf(AgricultureReportFactory())
    }

    override val type = Companion.type

    val territories: Collection<Territory>

    constructor(territories: Collection<Territory>): super(reports) {
        this.territories = territories
    }

    constructor(other: TerritoryLogicModule): super(reports){
        this.territories = other.territories.map { Territory(it) }
    }

    constructor(saveString: Map<String, Any>, game: Game): super(reports){
        this.territories = listOf()
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