package game.action

import game.Game
import game.Character

class Action {
    val type: ActionType

    constructor(type: ActionType){
        this.type = type
    }

    override fun toString(): String{
        return type.toString()
    }

    override fun equals(other: Any?): Boolean {
        if(other is Action){
            return type == other.type
        }
        return false
    }

    abstract class ActionType{
        //checks prerequisites, and returns any effects produced
        abstract fun doAction(game: Game, player: Character): List<Effect>

    }
}