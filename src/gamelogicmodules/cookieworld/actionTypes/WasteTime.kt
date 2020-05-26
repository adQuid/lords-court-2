package gamelogicmodules.cookieworld.actionTypes

import game.action.Action
import game.Effect
import game.Game
import game.GameCharacter
import game.action.GlobalActionTypeFactory
import javafx.scene.Scene
import shortstate.ShortStateCharacter

class WasteTime: Action {

    companion object{
        val typeName: String
            get() = "WasteTime"
    }
    constructor(){

    }

    override fun doAction(game: Game, player: GameCharacter): List<Effect> {
        return listOf()
    }

    override fun toString(): String {
        return "be useless"
    }

    override fun description(): String {
        return "A filler action that does nothing."
    }

    override fun equals(other: Any?): Boolean {
        return other is WasteTime
    }

    override fun saveString(): Map<String, Any> {
        return hashMapOf(GlobalActionTypeFactory.TYPE_NAME to typeName)
    }
}