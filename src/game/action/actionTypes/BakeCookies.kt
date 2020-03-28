package game.action.actionTypes

import game.action.Action
import game.Effect
import game.effects.AddDelicousness
import game.Game
import game.GameCharacter
import game.action.GlobalActionTypeFactory

class BakeCookies: Action{

    constructor(){}

    override fun doAction(game: Game, player: GameCharacter): List<Effect> {
        return listOf(AddDelicousness(1.0))
    }

    override fun toString(): String {
        return "bake tasty cookies"
    }

    override fun equals(other: Any?): Boolean {
        return other is BakeCookies
    }

    override fun saveString(): Map<String, Any> {
        return hashMapOf(GlobalActionTypeFactory.TYPE_NAME to "BakeCookies")
    }

}