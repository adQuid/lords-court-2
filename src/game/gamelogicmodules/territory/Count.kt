package game.gamelogicmodules.territory

import game.GameCharacter
import game.Title
import game.action.Action
import game.action.actionTypes.BakeCookies
import game.action.actionTypes.WasteTime
import game.titlemaker.CookieWorldTitleFactory

class Count: Title{
    override val name: String
    override val reportsEntitled = listOf(AgricultureReportFactory())

    constructor(name: String){
       this.name = "Count of $name"
    }

    constructor(saveString: Map<String, Any>){
        this.name = saveString[NAME_NAME] as String
    }

    override fun clone(): Count {
        return Count(name)
    }

    override fun saveString(): Map<String, Any> {
        return hashMapOf(
            CookieWorldTitleFactory.TYPE_NAME to "Count",
            NAME_NAME to name,
            REPORTS_NAME to reportsEntitled.map { report -> report.toString() }
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