package shortstate.room.action

import shortstate.ShortStateGame
import shortstate.ShortStateCharacter
import shortstate.report.DeliciousnessReport
import shortstate.report.ReportType
import shortstate.report.generateReport
import shortstate.room.RoomAction

class MakeReport: RoomAction{

    val type: ReportType

    constructor(type: ReportType){
        this.type = type
    }

    override fun doAction(game: ShortStateGame, player: ShortStateCharacter) {
        println("looked up deliciousness")
        player.knownReports.add(generateReport(game.game, type))
        player.energy -= 10
    }

    override fun toString(): String {
        return "Look up $type"
    }
}