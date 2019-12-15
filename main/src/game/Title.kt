package game

import game.action.Action
import shortstate.report.ReportType

class Title {

    val name: String
    val actionsEntitled: List<Action.ActionType>
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
}