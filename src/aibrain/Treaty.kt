package aibrain

import game.Game

class Treaty {
    val deal: Deal

    constructor(deal: Deal){
        this.deal = deal
    }

    constructor(saveString: Map<String,Any>, game: Game){
        deal = Deal(saveString, game)
    }

    fun saveString(): Map<String, Any> {
        return deal.saveString()
    }
}