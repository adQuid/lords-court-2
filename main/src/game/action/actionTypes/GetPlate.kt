package game.action.actionTypes

import game.action.Action
import game.action.Effect
import game.Game
import game.Character

class GetPlate: Action.ActionType() {

    override fun doAction(game: Game, player: Character): List<Effect> {
        return listOf(ApplyPlate(1.0, player))
    }

    override fun toString(): String {
        return "get a plate"
    }

    override fun equals(other: Any?): Boolean {
        return other is GetPlate
    }

    class ApplyPlate: Effect {

        override var probability: Double
        val player: Character

        constructor(probability: Double, player: Character){
            this.probability = probability
            this.player = player
        }

        override fun equals(other: Any?): Boolean {
            if(other is ApplyPlate){
                return true
            }
            return false
        }
        override fun apply(game: Game) {
           game.hasPlate.add(player)
            player.dummyScore -= 0.45
        }
        override fun toString(): String{
            return "get plate for $player with probability $probability"
        }
    }
}