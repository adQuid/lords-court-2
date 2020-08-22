package shortstate.room.action

import shortstate.ShortStateGame
import shortstate.ShortStateCharacter
import shortstate.report.ReportFactory
import shortstate.room.RoomAction

class MakeReport: RoomAction{

    val factory: ReportFactory

    constructor(factory: ReportFactory ){
        this.factory = factory
    }

    override fun clickOn(game: ShortStateGame, player: ShortStateCharacter) {
        doActionIfCanAfford(game, player)
    }

    override fun cost(): Int {
        return factory.cost
    }

    override fun doAction(game: ShortStateGame, player: ShortStateCharacter) {
        println(player.player.name+" generates report: "+factory)
        player.knownReports.add(factory.generateReport(game.game))
    }

    override fun defocusAfter(): Boolean {
        return true
    }

    override fun toString(): String {
        return "Look up ${factory.tooltip()}"
    }
}