package shortstate.room.action

import game.action.Action
import game.action.actionTypes.BakeCookies
import main.UIGlobals
import shortstate.ShortStateGame
import shortstate.ShortStateCharacter
import shortstate.ShortStateController
import shortstate.room.RoomAction

class CommitToAction: RoomAction {

    val type: Action

    constructor(type: Action){
        this.type = type
    }

    override fun doAction(game: ShortStateGame, player: ShortStateCharacter) {
        player.prospectiveActions.add(type)
        player.energy -= 10
    }

    override fun defocusAfter(): Boolean {
        return true
    }

    override fun toString(): String {
        return "Decide to ${type.toString()}"
    }
}