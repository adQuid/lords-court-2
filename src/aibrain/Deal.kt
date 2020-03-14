package aibrain

import game.action.Action
import game.GameCharacter
import game.Game
import game.action.GlobalActionTypeFactory
import javafx.scene.Scene
import shortstate.ShortStateCharacter
import ui.Displayable
import ui.componentfactory.DealComponentFactory

abstract class Deal: Displayable {
    abstract fun theActions(): Map<GameCharacter, List<Action>>

    abstract fun dialogText(speaker: GameCharacter, target: GameCharacter): String

    abstract fun toFinishedDeal(): FinishedDeal
}

class FinishedDeal: Deal {

    val actions: Map<GameCharacter, List<Action>>

    val display: DealComponentFactory

    constructor(actions: Map<GameCharacter, List<Action>>){
        this.actions = actions
        display = DealComponentFactory(this)
    }

    constructor(saveString: Map<String, Any>, game: Game){
        val tempActions = mutableMapOf<GameCharacter, List<Action>>()
        (saveString["ACTIONS"] as Map<String, Any>).forEach { key, value -> tempActions[game.characterById(key.toInt())] =
                (value as List<Map<String,Any>>).map { map -> Action(GlobalActionTypeFactory.fromMap(map)) } }

        actions = tempActions
        display = DealComponentFactory(this)
    }

    override fun theActions(): Map<GameCharacter, List<Action>> {
        return actions
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

    fun toUnfinishedDeal(): UnfinishedDeal {
        return UnfinishedDeal(actions)
    }

    override fun dialogText(speaker: GameCharacter, target: GameCharacter): String {
        val retval = actions.map{(character, actions) -> "${character.name} will ${actions.toString()}"}

        return retval.joinToString(", ")
    }

    override fun display(perspective: ShortStateCharacter): Scene {
        return display.scenePage(perspective)
    }

    override fun toFinishedDeal(): FinishedDeal{
        return this
    }
}

class UnfinishedDeal: Deal{
    val actions: MutableMap<GameCharacter, MutableList<Action>>

    val display: DealComponentFactory

    override fun theActions(): Map<GameCharacter, List<Action>> {
        return actions.toMap().mapValues { entry -> entry.value.toList() }
    }

    constructor(players: List<GameCharacter>){
        actions = mutableMapOf()
        players.forEach {
            actions[it] = mutableListOf()
        }
        display = DealComponentFactory(this)
    }

    constructor(actions: Map<GameCharacter, List<Action>>){
        this.actions = mutableMapOf()
        actions.forEach{entry -> this.actions.put(entry.key, entry.value.toMutableList())}
        display = DealComponentFactory(this)
    }

    override fun toFinishedDeal(): FinishedDeal{
        return FinishedDeal(actions.toMap())
    }

    override fun dialogText(speaker: GameCharacter, target: GameCharacter): String {
        val retval = actions.map{(character, actions) -> "${character.name} will ${actions.toString()}"}

        return retval.joinToString(", ")
    }

    override fun display(perspective: ShortStateCharacter): Scene {
        return display.scenePage(perspective)
    }
}