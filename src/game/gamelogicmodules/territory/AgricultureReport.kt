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
    val wheat: Int
    
    constructor(game: Game){
        val logic = game.moduleOfType(TerritoryLogicModule.type) as TerritoryLogicModule
        wheat = logic.territories.first().wheat
    }

    constructor(saveString: Map<String, Any>){
        wheat = saveString["wheat"] as Int
    }

    override fun apply(game: Game) {
        val logic = game.moduleOfType(TerritoryLogicModule.type) as TerritoryLogicModule
        logic.territories.first().wheat = wheat
    }

    override fun toString(): String {
        return "This territory $wheat wheat"
    }

    override fun specialSaveString(): Map<String, Any> {
        return hashMapOf(
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

class AgricultureReportFactory: ReportFactory(){
    override val type = AgricultureReport.type

    override fun generateReport(game: Game): Report {
        return AgricultureReport(game)
    }

    override fun reportFromSaveString(saveString: Map<String, Any>): Report {
        return AgricultureReport(saveString)
    }

}