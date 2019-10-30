package game.action.actionTypes

import game.action.Action
import game.action.Effect
import game.Game
import game.Character

class BakeCookies: Action.ActionType() {
    override fun doAction(game: Game, player: Character): List<Effect> {
        return listOf(AddDelicousness(1.0))
    }

    override fun toString(): String {
        return "bake tasty cookies"
    }

    class AddDelicousness(override var probability: Double) : Effect() {
        override fun equals(other: Any?): Boolean {
            if(other is AddDelicousness){
                return true
            }
            return false
        }
        override fun apply(game: Game) {
           game.deliciousness++
        }
        override fun toString(): String{
            return "add deliciousness with probability $probability"
        }
    }
}