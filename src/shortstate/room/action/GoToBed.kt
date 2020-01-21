package shortstate.room.action

import main.Controller
import shortstate.ShortStateGame
import shortstate.ShortStateCharacter
import shortstate.ShortStateController
import shortstate.room.RoomAction

class GoToBed: RoomAction() {
    override fun doAction(shortGameController: ShortStateController, player: ShortStateCharacter) {
        println("$player committed ${player.prospectiveActions.size} actions")
        player.energy = 0
        shortGameController.endScene(shortGameController.shortGame.sceneForPlayer(player)!!)
        Controller.singleton!!.commitActionsForPlayer(player.player, player.prospectiveActions)
    }

    override fun toString(): String {
        return "Go to Bed (End Turn)"
    }
}