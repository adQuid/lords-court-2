package game.titles

import game.GameCharacter
import game.Title
import game.action.Action
import gamelogicmodules.cookieworld.actionTypes.GetMilk
import game.titlemaker.CookieWorldTitleFactory
import shortstate.report.DeliciousnessReportFactory
import shortstate.report.ReportFactory

class Milkman: Title{
    override val name: String
    override val reportsEntitled: List<ReportFactory>

    constructor(){
       this.name = "Milkman"
       reportsEntitled = listOf(DeliciousnessReportFactory())
    }

    constructor(saveString: Map<String, Any>){
        this.name = saveString[NAME_NAME] as String
        reportsEntitled = listOf(DeliciousnessReportFactory())
    }

    override fun clone(): Milkman {
        return Milkman()
    }

    override fun saveString(): Map<String, Any> {
        return hashMapOf(
            CookieWorldTitleFactory.TYPE_NAME to "Milkman",
            NAME_NAME to name,
            REPORTS_NAME to reportsEntitled.map { report -> report.toString() }
        )
    }

    override fun actionsReguarding(players: List<GameCharacter>): List<Action> {
        return players.map { player -> GetMilk(player) }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as Milkman

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