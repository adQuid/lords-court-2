package aibrain

import aibrain.conversationadvocates.ConversationAdvocate
import aibrain.conversationadvocates.FarewellAdvocate
import aibrain.conversationadvocates.OfferDealAdvocate
import aibrain.conversationadvocates.OfferWritAdvocate
import shortstate.dialog.Line
import game.Game
import game.GameCharacter
import shortstate.ShortStateCharacter
import shortstate.dialog.linetypes.*

class ConversationBrain {

    val shortCharacter: ShortStateCharacter

    val dealsDiscussed = mutableMapOf<GameCharacter,MutableList<Deal>>()

    private val conversationAdvocates: List<ConversationAdvocate>


    constructor(shortCharacter: ShortStateCharacter){
        this.shortCharacter = shortCharacter
        conversationAdvocates = listOf(FarewellAdvocate(shortCharacter), OfferDealAdvocate(shortCharacter), OfferWritAdvocate(shortCharacter))
    }

    fun putOrAddDealToMemory(player: GameCharacter, deal: Deal){
        if(dealsDiscussed.containsKey(player)){
            dealsDiscussed[player]!!.add(deal)
        } else {
            dealsDiscussed[player] = mutableListOf(deal)
        }
    }

    fun startConversation(target: GameCharacter, game: Game): Line {
        return conversationAdvocates.sortedByDescending { adv -> adv.weight(game, target) }[0].line(target)
    }

}