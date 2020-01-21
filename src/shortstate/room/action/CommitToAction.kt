package shortstate.room.action

import game.action.Action
import game.action.actionTypes.BakeCookies
import shortstate.ShortStateGame
import shortstate.ShortStateCharacter
import shortstate.ShortStateController
import shortstate.room.RoomAction

class CommitToAction: RoomAction {

    val type: Action.ActionType

    constructor(type: Action.ActionType){
        this.type = type
    }

    override fun doAction(game: ShortStateController, player: ShortStateCharacter) {
        player.prospectiveActions.add(Action(type))
        player.energy -= 10
    }

    override fun toString(): String {
        return "Decide to ${type.toString()}"
    }
}