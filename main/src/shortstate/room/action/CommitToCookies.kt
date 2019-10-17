package shortstate.room.action

import action.Action
import actionTypes.BakeCookies
import main.Controller
import shortstate.ShortStateGame
import shortstate.ShortStatePlayer
import shortstate.room.RoomAction

class CommitToCookies: RoomAction() {
    override fun doAction(game: ShortStateGame, player: ShortStatePlayer) {
        player.prospectiveActions.add(Action(BakeCookies()))
        Controller.singleton!!.commitActionsForPlayer(player.player, player.prospectiveActions)
        player.energy -= 10
    }

    override fun toString(): String {
        return "Decide to bake cookies"
    }
}