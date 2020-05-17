package game

import game.action.Action
import shortstate.report.ReportFactory
import shortstate.report.ReportType
import shortstate.room.RoomActionMaker
import shortstate.room.action.MakeReport
import shortstate.room.actionmaker.DefaultRoomActionMaker

abstract class Title {

    val NAME_NAME = "name"
    abstract val name: String
    val REPORTS_NAME = "reports"
    abstract val reportsEntitled: List<ReportFactory>

    abstract fun saveString(): Map<String, Any>

    override fun equals(other: Any?): Boolean {
        if(other is Title){
            return this.name == other.name &&
            this.reportsEntitled == other.reportsEntitled
        } else {
            return false
        }
    }

    override fun toString(): String {
        return name
    }

    abstract fun clone(): Title

    abstract fun actionsReguarding(players: List<GameCharacter>): List<Action>

    fun reportActions(): List<RoomActionMaker>{
        return reportsEntitled.map { type -> DefaultRoomActionMaker(MakeReport(type)) }
    }
}