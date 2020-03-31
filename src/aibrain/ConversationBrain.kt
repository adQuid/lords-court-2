package aibrain

import shortstate.dialog.Line
import game.Game
import game.GameCharacter
import shortstate.ShortStateCharacter
import shortstate.dialog.linetypes.*

class ConversationBrain {

    val shortCharacter: ShortStateCharacter

    private val dealsDiscussed = mutableMapOf<GameCharacter,MutableList<Deal>>()

    constructor(shortCharacter: ShortStateCharacter){
        this.shortCharacter = shortCharacter
    }

    fun putOrAddDealToMemory(player: GameCharacter, deal: Deal){
        if(dealsDiscussed.containsKey(player)){
            dealsDiscussed[player]!!.add(deal)
        } else {
            dealsDiscussed[player] = mutableListOf(deal)
        }
    }

    fun startConversation(target: GameCharacter, game: Game): Line {
        val dealToOffer = shortCharacter.player.brain.dealsILike!!.filter { it.theActions().keys.contains(target) }[0]

        val thingToSuggest = shortCharacter.player.brain.lastCasesOfConcern!!
            .filter{case -> case.valueToCharacter(shortCharacter.player.brain.player) > 0}
            .filter{ case->case.plan.player == target}

        if(dealToOffer != null && !dealsDiscussed.getOrDefault(target, mutableListOf()).contains(dealToOffer.toFinishedDeal())){
            return OfferDeal(dealToOffer)
        }

        return Approve()
    }

}