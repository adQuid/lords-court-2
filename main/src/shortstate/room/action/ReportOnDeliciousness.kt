package shortstate.room.action

import shortstate.ShortStateGame
import shortstate.ShortStateCharacter
import shortstate.report.DeliciousnessReport
import shortstate.room.RoomAction

class ReportOnDeliciousness: RoomAction() {
    override fun doAction(game: ShortStateGame, player: ShortStateCharacter) {
        println("looked up deliciousness")
        player.knownReports.add(DeliciousnessReport(game.game.deliciousness))
        player.energy -= 10
    }

    override fun toString(): String {
        return "Look up Deliciousness levels"
    }
}