package game.gamelogicmodules.territory

import game.Game
import shortstate.report.Report
import shortstate.report.ReportFactory
import shortstate.report.ReportType

class AgricultureReport: Report {

    companion object{
        val type = "AgriculturalReport"
    }

    override val type: String = AgricultureReport.type
    val territory: Territory
    val wheat: Int
    
    constructor(game: Game, territory: Territory){
        this.territory = territory
        wheat = territory.wheat
    }

    constructor(saveString: Map<String, Any>, game: Game){
        val logic = game.moduleOfType(TerritoryLogicModule.type) as TerritoryLogicModule

        territory = logic.territoryById(saveString["territory"] as Int)
        wheat = saveString["wheat"] as Int
    }

    override fun apply(game: Game) {
        val logic = game.moduleOfType(TerritoryLogicModule.type) as TerritoryLogicModule
        logic.matchingTerritory(territory).wheat = wheat
    }

    override fun toString(): String {
        return "${territory.name} has $wheat wheat"
    }

    override fun specialSaveString(): Map<String, Any> {
        return hashMapOf(
            "territory" to territory.id,
            "wheat" to wheat
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AgricultureReport

        if (wheat != other.wheat) return false

        return true
    }

    override fun hashCode(): Int {
        return wheat.hashCode()
    }
}

class AgricultureReportFactory: ReportFactory{
    val territory: Territory
    override val type = AgricultureReport.type

    constructor(territory: Territory){
        this.territory = territory
    }

    override fun generateReport(game: Game): Report {
        val logic = game.moduleOfType(TerritoryLogicModule.type) as TerritoryLogicModule
        return AgricultureReport(game, territory)
    }

    override fun reportFromSaveString(saveString: Map<String, Any>, game: Game): Report {
        return AgricultureReport(saveString, game)
    }

}