package shortstate.room

import main.Controller
import shortstate.ShortStateGame
import shortstate.ShortStatePlayer

class GoToBed: RoomAction() {
    override fun doAction(game: ShortStateGame, player: ShortStatePlayer) {
        println("$player committed actions")
        Controller.singleton!!.commitActionsForPlayer(player.player, player.prospectiveActions)
    }

    override fun toString(): String {
        return "Go to Bed (End Turn)"
    }
}