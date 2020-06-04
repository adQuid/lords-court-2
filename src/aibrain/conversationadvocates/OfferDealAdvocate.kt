package aibrain.conversationadvocates

import aibrain.Deal
import game.Game
import game.GameCharacter
import shortstate.ShortStateCharacter
import shortstate.dialog.Line
import shortstate.dialog.linetypes.Farewell
import shortstate.dialog.linetypes.OfferDeal

class OfferDealAdvocate: ConversationAdvocate {

    constructor(perspective: ShortStateCharacter) : super(perspective) {
    }

    override fun weight(game: Game, target: GameCharacter): ConversationWeight {
        if(dealIWouldDiscuss(target) != null){
            return ConversationWeight(me.player.brain.dealsILike!![dealIWouldDiscuss(target)!!]!! * 80.0, line(target))
        } else {
            return ConversationWeight(0.0, Farewell())
        }
    }

    override fun line(target: GameCharacter): Line {
        val deal = dealIWouldDiscuss(target)
        if(deal != null) {
            return OfferDeal(deal)
        }
        return Farewell() //We should never be getting here
    }

    private fun dealIWouldDiscuss(target: GameCharacter): Deal?{
        val dealToOffer = me.player.brain.dealsILike!!.keys.filter { it.theActions().keys.contains(target) }.getOrNull(0)

        if(dealToOffer != null && !me.convoBrain.dealsDiscussed.getOrDefault(target, mutableListOf()).contains(dealToOffer.toFinishedDeal())){
            return dealToOffer
        }
        return null
    }
}