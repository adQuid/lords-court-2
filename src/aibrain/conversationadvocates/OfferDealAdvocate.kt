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

    override fun weight(game: Game, target: GameCharacter): Double {
        if(dealIWouldDiscuss(target) != null){
            return me.player.brain.dealValueToMe(dealIWouldDiscuss(target)!!) * 80.0
        } else {
            return 0.0
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
        val writsTargetHasSigned = me.player.writs.filter { it.signatories.contains(target) }
        val dealToOffer = me.player.brain.dealsILike!!.filter { it.theActions().keys.contains(target) && writsTargetHasSigned.filter { writ -> writ.deal == it }.isEmpty()}.getOrNull(0)

        val thingToSuggest = me.player.brain.lastCasesOfConcern!!
            .filter{case -> case.valueToCharacter(me.player.brain.player) > 0}
            .filter{ case-> case.plan.player == target}

        if(dealToOffer != null && !me.convoBrain.dealsDiscussed.getOrDefault(target, mutableListOf()).contains(dealToOffer.toFinishedDeal())){
            return dealToOffer
        }
        return null
    }
}