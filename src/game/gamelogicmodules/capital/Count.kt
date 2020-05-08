package game.gamelogicmodules.capital

import game.GameCharacter
import game.Title
import game.action.Action
import game.action.actionTypes.BakeCookies
import game.action.actionTypes.WasteTime
import game.gamelogicmodules.territory.ActiveCropsReportFactory
import game.gamelogicmodules.territory.FoodStocksReportFactory
import game.gamelogicmodules.territory.Territory
import game.titlemaker.CookieWorldTitleFactory
import shortstate.report.ReportFactory

class Count: Title{
    override val name: String
    val CAPITAL_NAME = "Capital"
    val capital: Capital
    override val reportsEntitled: List<ReportFactory>

    constructor(capital: Capital){
        this.capital = capital
        this.name = "Count of ${capital.territory!!}"
        reportsEntitled = listOf(
            FoodStocksReportFactory(capital.territory!!.id),
            ActiveCropsReportFactory(capital.territory!!.id),
            CapitalStocksReportFactory(capital)
        )
    }

    constructor(saveString: Map<String, Any>){
        this.name = saveString[NAME_NAME] as String
        this.capital = Capital(saveString[CAPITAL_NAME] as Map<String, Any>)
        reportsEntitled = listOf(
            FoodStocksReportFactory(capital.territory!!.id),
            ActiveCropsReportFactory(capital.territory!!.id),
            CapitalStocksReportFactory(capital)
        )
    }

    override fun clone(): Count {
        return Count(capital)
    }

    override fun saveString(): Map<String, Any> {
        return hashMapOf(
            CookieWorldTitleFactory.TYPE_NAME to "Count",
            NAME_NAME to name,
            REPORTS_NAME to reportsEntitled.map { report -> report.toString() },
            CAPITAL_NAME to capital.saveString()
        )
    }

    override fun actionsReguarding(players: List<GameCharacter>): List<Action> {
        return listOf(BakeCookies(), WasteTime())
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as Count

        if (name != other.name) return false
        if (reportsEntitled != other.reportsEntitled) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + reportsEntitled.hashCode()
        return result
    }
}