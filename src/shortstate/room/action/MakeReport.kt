package shortstate.room.action

import main.UIGlobals
import shortstate.GameRules
import shortstate.ShortStateGame
import shortstate.ShortStateCharacter
import shortstate.ShortStateController
import shortstate.report.ReportFactory
import shortstate.report.ReportType
import shortstate.room.RoomAction

class MakeReport: RoomAction{

    val factory: ReportFactory

    constructor(factory: ReportFactory ){
        this.factory = factory
    }

    override fun clickOn(game: ShortStateGame, player: ShortStateCharacter) {
        doAction(game, player)
    }

    override fun doAction(game: ShortStateGame, player: ShortStateCharacter) {
        player.knownReports.add(factory.generateReport(game.game))
        player.energy -= GameRules.COST_TO_MAKE_REPORT
    }

    override fun defocusAfter(): Boolean {
        return true
    }

    override fun toString(): String {
        return "Look up ${factory.tooltip()}"
    }
}