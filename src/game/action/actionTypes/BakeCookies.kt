package game.action.actionTypes

import game.action.Action
import game.action.Effect
import game.Game
import game.Character
import game.action.GlobalActionTypeFactory

class BakeCookies: Action.ActionType() {
    override fun doAction(game: Game, player: Character): List<Effect> {
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

    class AddDelicousness(override var probability: Double) : Effect() {
        override fun equals(other: Any?): Boolean {
            if(other is AddDelicousness){
                return true
            }
            return false
        }
        override fun apply(game: Game) {
           game.deliciousness += this.probability
        }
        override fun toString(): String{
            return "add deliciousness with probability $probability"
        }

        override fun saveString(): Map<String, Any> {
            return hashMapOf(
                TYPE_NAME to "BakeCookies"
            )
        }

        override fun describe(): String {
            return "deliciousness levels would increase by {probability}"
        }
    }
}