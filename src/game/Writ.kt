package game

import aibrain.Deal
import aibrain.FinishedDeal

//The in-game representation of a group of actions
class Writ {
    val deal: FinishedDeal
    val signatories: MutableList<GameCharacter>

    constructor(deal: Deal){
        this.deal = deal.toFinishedDeal()
        signatories = mutableListOf()
    }

    constructor(deal: Deal, signatories: List<GameCharacter>){
        this.deal = deal.toFinishedDeal()
        this.signatories = signatories.toMutableList()
    }

    constructor(other: Writ){
        this.deal = other.deal //finished deals are immutable
        this.signatories = other.signatories //not sure if we should be storing the whole object here anyway...
    }

    constructor(saveString: Map<String,Any>, game: Game){
        deal = FinishedDeal(saveString["deal"] as Map<String, Any>, game)
        signatories = (saveString["signatories"] as List<Int>).map {id -> game.characterById(id)}.toMutableList()
    }

    fun saveString(): Map<String, Any> {
        return mutableMapOf<String, Any>(
            "deal" to deal.saveString(),
            "signatories" to signatories.map { it.id }
        )
    }
}