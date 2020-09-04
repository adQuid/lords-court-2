package gamelogic.government

import game.Game
import shortstate.ShortStateCharacter
import shortstate.ShortStateGame
import shortstate.report.Report
import shortstate.report.ReportFactory
import gamelogic.territory.Territory

class CapitalStocksReport: Report {

    companion object{
        val type = "CapitalReport"
    }

    override val type: String = CapitalStocksReport.type
    val capital: Capital
    val resources: Map<String, Int>
    
    constructor(game: Game, capital: Capital){
        this.capital = capital
        resources = capital.resources.resources
    }

    constructor(saveString: Map<String, Any>, game: Game){
        val logic = game.moduleOfType(GovernmentLogicModule.type) as GovernmentLogicModule

        capital = logic.capitalById(saveString["id"] as Int)
        resources = capital.resources.resources.toMap()
    }

    override fun apply(game: Game) {
        val logic = game.moduleOfType(GovernmentLogicModule.type) as GovernmentLogicModule
        resources.forEach { logic.matchingCapital(capital).resources.set(it.key, it.value) }
    }

    override fun prettyPrint(context: ShortStateGame, perspective: ShortStateCharacter): String {
        return "As of ${context.game.turnName()}, The larders at ${capital.territory!!.name} have..."
    }

    override fun detailedDescription(): String {
        return "${resources.map { "${it.key}: ${it.value}" }} \n  These resources are in your treasury, and will be available in the event of siege."
    }

    override fun specialSaveString(): Map<String, Any> {
        return hashMapOf(
            "id" to capital.terId,
            "resources" to resources
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CapitalStocksReport

        if (type != other.type) return false
        if (capital != other.capital) return false
        if (resources != other.resources) return false

        return true
    }

    override fun hashCode(): Int {
        var result = type.hashCode()
        result = 31 * result + capital.hashCode()
        result = 31 * result + resources.hashCode()
        return result
    }


}

class CapitalStocksReportFactory: ReportFactory{
    val capital: Capital
    override val type = CapitalStocksReport.type
    override val cost: Int
        get() = 15

    constructor(capital: Capital){
        this.capital = capital
    }

    override fun generateReport(game: Game): Report {
        return CapitalStocksReport(game, capital)
    }

    override fun reportFromSaveString(saveString: Map<String, Any>, game: Game): Report {
        return CapitalStocksReport(saveString, game)
    }

    override fun tooltip(): String {
        return "Capital Stocks Report"
    }
}