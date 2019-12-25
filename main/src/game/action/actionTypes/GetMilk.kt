package game.action.actionTypes

import game.action.Action
import game.action.Effect
import game.Game
import game.Character

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

    class AddMilk: Effect {

        override var probability: Double
        val player: Character

        constructor(probability: Double, player: Character){
            this.probability = probability
            this.player = player
        }

        override fun equals(other: Any?): Boolean {
            if(other is AddMilk){
                return true
            }
            return false
        }
        override fun apply(game: Game) {
           game.hasMilk.add(player)
            player.dummyScore -= 0.45
        }
        override fun toString(): String{
            return "get milk for $player with probability $probability"
        }

        override fun describe(): String {
            return "${player} would get milk"
        }
    }
}