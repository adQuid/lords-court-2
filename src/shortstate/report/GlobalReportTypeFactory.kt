package shortstate.report

import game.Character
import game.action.Action
import game.action.actionTypes.BakeCookies
import game.action.actionTypes.GetMilk
import game.action.actionTypes.WasteTime

object GlobalReportTypeFactory {
    val TYPE_NAME = "TYPE"
    val typeMap: HashMap<String, (map: Map<String, Any>) -> Report> = hashMapOf(
        "DeliciousnessReport" to {map -> DeliciousnessReport(map) }
    )

    fun fromMap(map: Map<String, Any>): Report {
        return typeMap[map[TYPE_NAME]]!!(map)
    }
}