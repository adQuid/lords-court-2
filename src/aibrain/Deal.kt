package aibrain

import game.action.Action
import game.GameCharacter
import game.Game
import game.action.GlobalActionTypeFactory

class Deal {

    val actions: Map<GameCharacter, List<Action>>

    constructor(actions: Map<GameCharacter, List<Action>>){
        this.actions = actions
    }

    constructor(saveString: Map<String, Any>, game: Game){
        val tempActions = mutableMapOf<GameCharacter, List<Action>>()
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

    fun dialogText(speaker: GameCharacter, target: GameCharacter): String {
        val retval = actions.map{(character, actions) -> "${character.name} will ${actions.toString()}"}

        return retval.joinToString(", ")
    }
}