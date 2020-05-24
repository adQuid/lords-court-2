package shortstate.room.action

import game.action.Action
import shortstate.ShortStateGame
import shortstate.ShortStateCharacter
import shortstate.room.RoomAction

class CommitToAction: RoomAction {

    val type: Action

    constructor(type: Action){
        this.type = type
    }

    override fun cost(): Int {
        return 0
    }

    override fun clickOn(game: ShortStateGame, player: ShortStateCharacter) {
        doActionIfCanAfford(game, player)
    }

    override fun doAction(game: ShortStateGame, player: ShortStateCharacter) {
        game.game.appendActionsForPlayer(player.player, listOf(type))
    }

    override fun defocusAfter(): Boolean {
        return true
    }

    override fun toString(): String {
        return "Decide to ${type.toString()}"
    }
}