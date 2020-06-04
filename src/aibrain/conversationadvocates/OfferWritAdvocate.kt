package aibrain.conversationadvocates

import game.Game
import game.GameCharacter
import game.Writ
import shortstate.ShortStateCharacter
import shortstate.dialog.Line
import shortstate.dialog.linetypes.Farewell
import shortstate.dialog.linetypes.OfferWrit

class OfferWritAdvocate: ConversationAdvocate {

    constructor(perspective: ShortStateCharacter) : super(perspective) {
    }

    override fun weight(game: Game, target: GameCharacter): ConversationWeight {
        if(writIWouldDiscuss(target) != null){
            return ConversationWeight(me.player.brain.dealsILike!!.getOrDefault(writIWouldDiscuss(target)!!.deal, 0.0) * 100.0, line(target))
        } else {
            return ConversationWeight(0.0, Farewell())
        }
    }

    override fun line(target: GameCharacter): Line {
        val writ = writIWouldDiscuss(target)
        if(writ != null) {
            return OfferWrit(writ)
        }
        return Farewell() //We should never be getting here
    }

    private fun writIWouldDiscuss(target: GameCharacter): Writ?{
        return me.player.writs
            .filter { writ -> !writ.complete() && writ.deal.actions.keys.contains(target) && me.player.brain.dealsILike!!.containsKey(writ.deal) }
            .sortedByDescending { writ -> me.player.brain.dealValueToMe(writ.deal) }.getOrNull(0)
    }
}