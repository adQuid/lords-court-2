package aibrain

import game.action.Action
import game.GameCharacter
import game.Game
import game.action.GlobalActionTypeFactory
import javafx.scene.Scene
import shortstate.ShortStateCharacter
import ui.PerspectiveDisplayable
import ui.componentfactory.DealComponentFactory

abstract class Deal: PerspectiveDisplayable() {
    abstract fun theActions(): Map<GameCharacter, Set<Action>>

    abstract fun dialogText(speaker: GameCharacter, target: GameCharacter): String

    abstract fun toFinishedDeal(): FinishedDeal
}

class FinishedDeal: Deal {

    val actions: Map<GameCharacter, Set<Action>>

    val display: DealComponentFactory

    constructor(actions: Map<GameCharacter, Set<Action>>){
        this.actions = actions
        display = DealComponentFactory(this)
    }

    constructor(saveString: Map<String, Any>, game: Game){
        val tempActions = mutableMapOf<GameCharacter, Set<Action>>()
        (saveString["ACTIONS"] as Map<String, Any>).forEach { key, value -> tempActions[game.characterById(key.toInt())] =
                (value as List<Map<String,Any>>).map { map -> GlobalActionTypeFactory.fromMap(map, game) }.toSet() }

        actions = tempActions
        display = DealComponentFactory(this)
    }

    override fun theActions(): Map<GameCharacter, Set<Action>> {
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

    override fun equals(other: Any?): Boolean {
        if(other is Deal){
            return this.actions == other.theActions()
        } else {
            return false
        }
    }

    override fun hashCode(): Int {
        return 5 //Is this wise for performance? NO. Will it get maps to work? Yes.
    }
}

class UnfinishedDeal: Deal{
    var actions: MutableMap<GameCharacter, MutableSet<Action>>

    val display: DealComponentFactory

    override fun theActions(): Map<GameCharacter, Set<Action>> {
        return actions.toMap().mapValues { entry -> entry.value.toMutableSet() }
    }

    constructor(players: List<GameCharacter>){
        actions = mutableMapOf()
        players.forEach {
            actions[it] = mutableSetOf()
        }
        display = DealComponentFactory(this)
    }

    constructor(actions: Map<GameCharacter, Set<Action>>){
        this.actions = mutableMapOf()
        actions.forEach{entry -> this.actions.put(entry.key, entry.value.toMutableSet())}
        display = DealComponentFactory(this)
    }

    override fun equals(other: Any?): Boolean {
        if(other is Deal){
            return this.actions == other.theActions()
        } else {
            return false
        }
    }

    override fun hashCode(): Int {
        return 5 //Is this wise for performance? NO. Will it get maps to work? Yes.
    }

    fun isEmpty(): Boolean{
        return actions.values.sumBy { list -> list.size } == 0
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