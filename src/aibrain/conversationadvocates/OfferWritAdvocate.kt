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

    override fun weight(game: Game, target: ShortStateCharacter): ConversationWeight {
        if(writIWouldDiscuss(target.player) != null){
            return ConversationWeight(me.player.brain.dealsILike!!.getOrDefault(writIWouldDiscuss(target.player)!!.deal, me.player.brain.dealValueToMe(writIWouldDiscuss(target.player)!!.deal)) * 100.0, line(game, target))
        } else {
            return ConversationWeight(0.0, Farewell())
        }
    }

    override fun line(game: Game, target: ShortStateCharacter): Line {
        val writ = writIWouldDiscuss(target.player)
        if(writ != null) {
            return OfferWrit(writ)
        }
        return Farewell() //We should never be getting here
    }

    private fun writIWouldDiscuss(target: GameCharacter): Writ?{
        return me.player.writs
            .filter { writ -> !writ.complete() && writ.deal.actions.keys.contains(target) && me.player.brain.dealValueToMe(writ.deal) > 0 }
            .sortedByDescending { writ -> me.player.brain.dealValueToMe(writ.deal) }.getOrNull(0)
    }
}