package game

import aibrain.Deal
import aibrain.FinishedDeal

//The in-game representation of a group of actions
class Writ {
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
        return "$name (signed by ${signatories.fold("", { acc, character -> acc +", "+character.toString()}).drop(2)})"
    }

    fun complete(): Boolean{
        return deal.actions.keys.equals(signatories.toSet())
    }
}