package actionTypes

import action.Action
import action.Effect
import game.Game
import game.Player

class WasteTime: Action.ActionType() {
    override fun doAction(game: Game, player: Player): List<Effect> {
        return listOf()
    }

    override fun toString(): String {
        return "be useless"
    }
}