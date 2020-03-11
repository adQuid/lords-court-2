package aibrain

import game.Game

class Treaty {
    val deal: FinishedDeal

    constructor(deal: FinishedDeal){
        this.deal = deal
    }

    constructor(saveString: Map<String,Any>, game: Game){
        deal = FinishedDeal(saveString, game)
    }

    fun saveString(): Map<String, Any> {
        return deal.saveString()
    }
}