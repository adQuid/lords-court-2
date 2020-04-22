package game.titles

import game.GameCharacter
import game.Title
import game.action.Action
import game.action.actionTypes.BakeCookies
import game.action.actionTypes.WasteTime
import game.titlemaker.TitleFactory
import shortstate.report.ReportType

class Baker: Title{
    override val name: String
    override val reportsEntitled: List<ReportType>

    constructor(name: String){
       this.name = "Baker of $name"
       reportsEntitled = listOf(ReportType.DeliciousnessReportType)
    }

    constructor(saveString: Map<String, Any>){
        this.name = saveString[NAME_NAME] as String
        reportsEntitled = listOf(ReportType.DeliciousnessReportType)
    }

    override fun clone(): Baker {
        return Baker(name)
    }

    override fun saveString(): Map<String, Any> {
        return hashMapOf(
            TitleFactory.TYPE_NAME to "Count",
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

        other as Baker

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