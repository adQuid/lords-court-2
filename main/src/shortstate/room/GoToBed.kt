package shortstate.room

import shortstate.ShortStateGame
import shortstate.ShortStatePlayer

class GoToBed: RoomAction() {
    override fun doAction(game: ShortStateGame, player: ShortStatePlayer) {
        println("this happened")
        game.game.commitActionsForPlayer(player.player, player.prospectiveActions)
    }

    override fun toString(): String {
        return "Go to Bed (End Turn)"
    }
}