package shortstate.room.action

import shortstate.ShortStateGame
import shortstate.ShortStateCharacter
import shortstate.ShortStateController
import shortstate.report.ReportType
import shortstate.report.generateReport
import shortstate.room.RoomAction

class MakeReport: RoomAction{

    val type: ReportType

    constructor(type: ReportType){
        this.type = type
    }

    override fun doAction(game: ShortStateController, player: ShortStateCharacter) {
        println("looked up deliciousness")
        player.knownReports.add(generateReport(game.shortGame.game, type))
        player.energy -= 10
    }

    override fun toString(): String {
        return "Look up $type"
    }
}