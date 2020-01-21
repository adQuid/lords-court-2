package game.action.actionTypes

import game.action.Action
import game.Effect
import game.effects.AddMilk
import game.Game
import game.Character
import game.action.GlobalActionTypeFactory

class GetMilk: Action.ActionType {

    val player: Character

    constructor(player: Character){
        this.player = player
    }

    override fun doAction(game: Game, player: Character): List<Effect> {
        return listOf(AddMilk(1.0, this.player))
    }

    override fun toString(): String {
        return "get milk for ${player.name}"
    }

    override fun equals(other: Any?): Boolean {
        return other is GetMilk
    }

    override fun saveString(): Map<String, Any> {
        return hashMapOf(
            GlobalActionTypeFactory.TYPE_NAME to "WasteTime",
            "player" to player.id
        )
    }
}