package gamelogic.cookieworld.actionTypes

import game.action.Action
import game.Game
import game.GameCharacter
import game.action.GlobalActionTypeFactory
import shortstate.ShortStateCharacter

class WasteTime: Action {

    companion object{
        val typeName: String
            get() = "WasteTime"
    }
    constructor(){

    }

    override fun doAction(game: Game, player: GameCharacter) {
        //do nothing
    }

    override fun tooltip(perspective: ShortStateCharacter): String {
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

    override fun collidesWith(other: Action): Boolean {
        return other is WasteTime
    }
}