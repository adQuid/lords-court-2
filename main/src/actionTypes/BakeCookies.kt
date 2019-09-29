package actionTypes

import action.Action
import action.Effect
import game.Game
import game.Player

class BakeCookies: Action.ActionType() {
    override fun doAction(game: Game, player: Player): List<Effect> {
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