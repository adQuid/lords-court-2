package game.titlemaker

import game.Title
import game.action.actionTypes.BakeCookies
import game.action.actionTypes.WasteTime
import shortstate.report.ReportType

object TitleFactory {

    fun makeCountTitle(name: String): Title {
        return Title("Count of $name",
            listOf(BakeCookies(), WasteTime()),
            listOf(ReportType.DeliciousnessReportType))
    }

}