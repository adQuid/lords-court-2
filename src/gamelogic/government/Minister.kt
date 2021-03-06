package gamelogic.government

import game.Game
import game.GameCharacter
import game.Title
import game.action.Action
import gamelogic.territory.ActiveCropsReportFactory
import gamelogic.territory.FoodStocksReportFactory
import game.titlemaker.CookieWorldTitleFactory
import gamelogic.territory.PopulationReportFactory
import shortstate.report.ReportFactory
import ui.specialdisplayables.selectionmodal.Tab

class Minister: Title{
    override val importance = 5
    override val name: String
    val CAPITAL_NAME = "Capital"
    val capital: Capital
    override val reportsEntitled: List<ReportFactory>

    constructor(capital: Capital){
        this.capital = capital
        this.name = "Minister of ${capital.territory!!.name}"
        reportsEntitled = reportsEntitled()
    }

    constructor(saveString: Map<String, Any>, game: Game){
        val capitalLogic = game.moduleOfType(GovernmentLogicModule.type) as GovernmentLogicModule
        this.name = saveString[NAME_NAME] as String
        this.capital = capitalLogic.capitalById(saveString[CAPITAL_NAME] as Int)
        reportsEntitled = reportsEntitled()
    }

    private fun reportsEntitled(): List<ReportFactory>{
        return listOf(
            FoodStocksReportFactory(capital.terId),
            ActiveCropsReportFactory(capital.terId),
            PopulationReportFactory(capital.terId),
            CapitalStocksReportFactory(capital)
        )
    }

    override fun clone(): Minister {
        return Minister(capital)
    }

    override fun saveString(): Map<String, Any> {
        return hashMapOf(
            CookieWorldTitleFactory.TYPE_NAME to "Minister",
            NAME_NAME to name,
            REPORTS_NAME to reportsEntitled.map { report -> report.toString() },
            CAPITAL_NAME to capital.terId
        )
    }

    override fun actionsReguarding(players: List<GameCharacter>): List<Tab<Action>> {
        return listOf()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as Minister

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