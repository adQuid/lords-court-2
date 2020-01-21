package aibrain

import game.action.Action
import game.Character
import game.Game
import game.action.GlobalActionTypeFactory

class Deal {

    val actions: Map<Character, List<Action>>

    constructor(actions: Map<Character, List<Action>>){
        this.actions = actions
    }

    constructor(saveString: Map<String, Any>, game: Game){
        val tempActions = mutableMapOf<Character, List<Action>>()
        (saveString["ACTIONS"] as Map<String, Any>).forEach { key, value -> tempActions[game.characterById(key.toInt())] =
                (value as List<Map<String,Any>>).map { map -> Action(GlobalActionTypeFactory.fromMap(map)) } }

        actions = tempActions
    }

   fun saveString(): Map<String, Any> {
       val actionMap = mutableMapOf<String, Any>()
       actions.forEach { (character, actions) -> actionMap[character.id.toString()] = actions.map{action -> action.saveString()}}

       return hashMapOf(
           "ACTIONS" to actionMap
       )
   }

    override fun toString(): String {
        return "[DEAL]"
    }

    fun dialogText(speaker: Character, target: Character): String {
        val retval = actions.map{(character, actions) -> "${character.name} will ${actions.toString()}"}

        return retval.joinToString(", ")
    }
}