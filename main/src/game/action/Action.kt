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

    fun saveString(): Map<String, Any>{
        return type.saveString()
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

        abstract fun saveString(): Map<String, Any>
    }
}