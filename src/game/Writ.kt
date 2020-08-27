package game

import aibrain.Deal
import aibrain.FinishedDeal
import javafx.scene.Scene
import shortstate.ShortStateCharacter
import ui.Displayable
import ui.componentfactory.WritComponentFactory

//The in-game representation of a group of actions
class Writ: Displayable {

    val display = WritComponentFactory(this)

    override fun universalDisplay(perspective: ShortStateCharacter?): Scene {
        return display.scenePage(perspective!!)
    }

    var name: String
    val deal: FinishedDeal
    val signatories: MutableList<GameCharacter>

    constructor(name: String, deal: Deal, signatories: List<GameCharacter>){
        this.name = name
        this.deal = deal.toFinishedDeal()
        this.signatories = signatories.toMutableList()
    }

    constructor(other: Writ){
        this.name = other.name
        this.deal = other.deal //finished deals are immutable
        this.signatories = other.signatories //not sure if we should be storing the whole object here anyway...
    }

    constructor(saveString: Map<String,Any>, game: Game){
        name = saveString["name"] as String
        deal = FinishedDeal(saveString["deal"] as Map<String, Any>, game)
        signatories = (saveString["signatories"] as List<Int>).map {id -> game.characterById(id)}.toMutableList()
    }

    fun saveString(): Map<String, Any> {
        return mutableMapOf<String, Any>(
            "name" to name,
            "deal" to deal.saveString(),
            "signatories" to signatories.map { it.id }
        )
    }

    override fun toString(): String {
        return "$name (signed by ${prettyPrintSignatories()})"
    }

    fun prettyPrintSignatories(): String{
        return signatories.fold("", { acc, character -> acc +", "+character.toString()}).drop(2)
    }

    fun complete(): Boolean{
        return deal.actions.keys.equals(signatories.toSet())
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Writ

        if (name != other.name) return false
        if (deal != other.deal) return false
        if (signatories != other.signatories) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + deal.hashCode()
        result = 31 * result + signatories.hashCode()
        return result
    }


}