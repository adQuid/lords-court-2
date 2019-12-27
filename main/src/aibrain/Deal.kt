package aibrain

import game.action.Action
import game.Character

class Deal {

    val actions: Map<Character, List<Action>>

    constructor(actions: Map<Character, List<Action>>){
        this.actions = actions
    }

    override fun toString(): String {
        return "[DEAL]"
    }

   fun dialogText(speaker: Character, target: Character): String {
        val retval = actions.map{(character, actions) -> "${character.name} will ${actions.toString()}"}

       return retval.joinToString(", ")
    }

}