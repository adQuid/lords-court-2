package game

import game.action.Action
import game.action.GlobalActionTypeFactory
import shortstate.report.ReportType

class Title {

    val NAME_NAME = "name"
    val name: String
    val ACTIONS_NAME = "actions"
    val actionsEntitled: List<Action.ActionType>
    val REPORTS_NAME = "reports"
    val reportsEntitled: List<ReportType>

    constructor(name: String, actions: List<Action.ActionType>, reports: List<ReportType>){
        this.name = name
        actionsEntitled = actions
        reportsEntitled = reports
    }

    constructor(other: Title){
        this.name = other.name
        actionsEntitled = other.actionsEntitled
        reportsEntitled = other.reportsEntitled
    }

    override fun toString(): String {
        return name
    }

    constructor(saveString: Map<String, Any>) {
        name = saveString[NAME_NAME] as String
        actionsEntitled = (saveString[ACTIONS_NAME] as List<Map<String, Any>>).map { map -> GlobalActionTypeFactory.fromMap(map) }
        reportsEntitled = (saveString[REPORTS_NAME] as List<String>).map { str -> ReportType.valueOf(str)}
    }

    fun saveString(): Map<String, Any> {
        return hashMapOf(
            NAME_NAME to name,
            ACTIONS_NAME to actionsEntitled.map { action -> action.saveString() },
            REPORTS_NAME to reportsEntitled.map { report -> report.toString() }
        )
    }
}