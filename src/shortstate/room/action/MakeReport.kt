package shortstate.room.action

import main.UIGlobals
import shortstate.GameRules
import shortstate.ShortStateGame
import shortstate.ShortStateCharacter
import shortstate.ShortStateController
import shortstate.report.ReportType
import shortstate.room.RoomAction

class MakeReport: RoomAction{

    val type: String

    constructor(type: String){
        this.type = type
    }

    override fun clickOn(game: ShortStateGame, player: ShortStateCharacter) {
        doAction(game, player)
    }

    override fun doAction(game: ShortStateGame, player: ShortStateCharacter) {
        println("looked up deliciousness")
        player.knownReports.add(game.game.reportFromType(type))
        player.energy -= GameRules.COST_TO_MAKE_REPORT
    }

    override fun defocusAfter(): Boolean {
        return true
    }

    override fun toString(): String {
        return "Look up ${UIGlobals.activeGame().reportFactoryFromType(type).tooltip()}"
    }
}