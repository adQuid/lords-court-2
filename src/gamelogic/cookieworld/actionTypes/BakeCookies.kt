package gamelogic.cookieworld.actionTypes

import game.action.Action
import game.Effect
import gamelogic.cookieworld.effects.AddDelicousness
import game.Game
import game.GameCharacter
import game.action.GlobalActionTypeFactory
import game.titles.Baker

class BakeCookies: Action{

    companion object{
        val typeName: String
            get() = "BakeCookies"
    }

    constructor(){}

    override fun doAction(game: Game, player: GameCharacter): List<Effect> {
        if(player.titles.filter { title -> title is Baker }.isNotEmpty()) {
            return listOf(AddDelicousness(1.0))
        }
        return listOf()
    }

    override fun toString(): String {
        return "bake tasty cookies"
    }

    override fun description(): String {
        return "Increases game deliciousness. Characters will still need milk to get points."
    }

    override fun saveString(): Map<String, Any> {
        return hashMapOf(GlobalActionTypeFactory.TYPE_NAME to typeName)
    }

    override fun equals(other: Any?): Boolean {
        return other is BakeCookies
    }

    override fun collidesWith(other: Action): Boolean{
        return other is BakeCookies
    }
}