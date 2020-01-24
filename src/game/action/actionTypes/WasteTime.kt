package game.action.actionTypes

import game.action.Action
import game.Effect
import game.Game
import game.GameCharacter
import game.action.GlobalActionTypeFactory

class WasteTime: Action.ActionType() {
    override fun doAction(game: Game, player: GameCharacter): List<Effect> {
        return listOf()
    }

    override fun toString(): String {
        return "be useless"
    }

    override fun equals(other: Any?): Boolean {
        return other is WasteTime
    }

    override fun saveString(): Map<String, Any> {
        return hashMapOf(GlobalActionTypeFactory.TYPE_NAME to "WasteTime")
    }
}