package aibrain.conversationadvocates

import aibrain.Deal
import game.Game
import game.GameCharacter
import shortstate.ShortStateCharacter
import shortstate.dialog.Line
import shortstate.dialog.linetypes.AbandonConversation
import shortstate.dialog.linetypes.Farewell
import shortstate.dialog.linetypes.OfferDeal

class OfferDealAdvocate: ConversationAdvocate {

    constructor(perspective: ShortStateCharacter) : super(perspective) {
    }

    override fun weight(game: Game, target: ShortStateCharacter): ConversationWeight {
        if(dealIWouldDiscuss(target.player) != null){
            return ConversationWeight(me.player.brain.dealsILike!![dealIWouldDiscuss(target.player)!!]!! * 80.0, line(game, target))
        } else {
            return ConversationWeight(0.0, Farewell())
        }
    }

    override fun line(game: Game, target: ShortStateCharacter): Line {
        val deal = dealIWouldDiscuss(target.player)
        if(deal != null) {
            return OfferDeal(deal)
        }
        return AbandonConversation() //We should never be getting here
    }

    private fun dealIWouldDiscuss(target: GameCharacter): Deal?{
        val dealToOffer = me.player.brain.dealsILike!!.keys.filter { it.theActions().keys.contains(target) }.getOrNull(0)

        if(dealToOffer != null && !me.convoBrain.dealsDiscussed.getOrDefault(target, mutableListOf()).contains(dealToOffer.toFinishedDeal())){
            return dealToOffer
        }
        return null
    }
}