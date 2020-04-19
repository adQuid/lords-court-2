package shortstate.room.action

import main.Controller
import shortstate.ShortStateGame
import shortstate.ShortStateCharacter
import shortstate.ShortStateController
import shortstate.room.RoomAction

class GoToBed: RoomAction() {
    override fun doAction(game: ShortStateGame, player: ShortStateCharacter) {
        println("$player committed ${player.prospectiveActions.size} actions")
        player.energy = 0
        game.sceneForPlayer(player)!!.terminated = true
        Controller.singleton!!.commitActionsForPlayer(player.player, player.prospectiveActions)
        Controller.singleton!!.concludeTurnForPlayer(player.player)
    }

    override fun defocusAfter(): Boolean {
        return false
    }

    override fun toString(): String {
        return "Go to Bed (End Turn)"
    }
}