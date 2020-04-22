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

    override fun weight(game: Game, target: GameCharacter): Double {
        if(writIWouldDiscuss(target) != null){
            return me.player.brain.dealValueToMe(writIWouldDiscuss(target)!!.deal) * 100.0
        } else {
            return 0.0
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
        return me.player.writs.sortedByDescending { writ -> !writ.complete() && me.player.brain.dealValueToMe(writ.deal) > 0 }.getOrNull(0)
    }
}