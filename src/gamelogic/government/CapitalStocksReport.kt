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
    val flour: Int
    val bread: Int
    
    constructor(game: Game, capital: Capital){
        this.capital = capital
        flour = capital.resources.get(Territory.FLOUR_NAME)
        bread = capital.resources.get(Territory.BREAD_NAME)
    }

    constructor(saveString: Map<String, Any>, game: Game){
        val logic = game.moduleOfType(GovernmentLogicModule.type) as GovernmentLogicModule

        capital = logic.capitalById(saveString["id"] as Int)
        flour = saveString["flour"] as Int
        bread = saveString["bread"] as Int
    }

    override fun apply(game: Game) {
        val logic = game.moduleOfType(GovernmentLogicModule.type) as GovernmentLogicModule
        logic.matchingCapital(capital).resources.set(Territory.FLOUR_NAME, flour)
        logic.matchingCapital(capital).resources.set(Territory.BREAD_NAME, bread)
    }

    override fun prettyPrint(context: ShortStateGame, perspective: ShortStateCharacter): String {
        return "As of ${context.game.turnName()}, The larders at ${capital.territory!!.name} has $flour flour and $bread bread"
    }

    override fun specialSaveString(): Map<String, Any> {
        return hashMapOf(
            "id" to capital.terId,
            "flour" to flour,
            "bread" to bread
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CapitalStocksReport

        if (capital != other.capital) return false
        if (flour != other.flour) return false
        if (bread != other.bread) return false

        return true
    }

    override fun hashCode(): Int {
        var result = capital.hashCode()
        result = 31 * result + flour
        result = 31 * result + bread
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