package game.action.actionTypes

import game.action.Action
import game.action.Effect
import game.Game
import game.Character

class WasteTime: Action.ActionType() {
    override fun doAction(game: Game, player: Character): List<Effect> {
        return listOf()
    }

    override fun toString(): String {
        return "be useless"
    }
}