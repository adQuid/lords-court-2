package action

import game.Game
import game.Player

class Action {
    val type: ActionType

    constructor(type: ActionType){
        this.type = type
    }

    override fun toString(): String{
        return type.toString()
    }

    abstract class ActionType{
        //checks prerequisites, and returns any effects produced
        abstract fun doAction(game: Game, player: Player): List<Effect>
    }
}