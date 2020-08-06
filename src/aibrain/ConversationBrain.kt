package aibrain

import aibrain.conversationadvocates.*
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
        conversationAdvocates = listOf(FarewellAdvocate(shortCharacter), OfferDealAdvocate(shortCharacter), OfferWritAdvocate(shortCharacter), SpecialLineAdvocate(shortCharacter))
    }

    fun putOrAddDealToMemory(player: GameCharacter, deal: Deal){
        if(dealsDiscussed.containsKey(player)){
            dealsDiscussed[player]!!.add(deal)
        } else {
            dealsDiscussed[player] = mutableListOf(deal)
        }
    }

    fun startConversation(target: GameCharacter, game: Game): Line {
        return conversationAdvocates.sortedByDescending { adv -> adv.weight(game, target).weight }[0].line(target)
    }

    fun bestWeight(target: GameCharacter, game: Game): Double {
        return conversationAdvocates.sortedByDescending { adv -> adv.weight(game, target).weight }[0].weight(game, target).weight
    }

}