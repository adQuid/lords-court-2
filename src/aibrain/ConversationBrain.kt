package aibrain

import shortstate.dialog.Line
import game.Game
import game.GameCharacter
import shortstate.ShortStateCharacter
import shortstate.dialog.linetypes.*

class ConversationBrain {

    val shortCharacter: ShortStateCharacter

    constructor(shortCharacter: ShortStateCharacter){
        this.shortCharacter = shortCharacter
    }

    fun startConversation(speaker: GameCharacter, game: Game): Line {
        val dealToOffer = shortCharacter.player.brain.dealsILike!!.filter { it.actions.keys.contains(speaker) }[0]

        val thingToSuggest = shortCharacter.player.brain.lastCasesOfConcern!!
            .filter{case -> case.valueToCharacter(shortCharacter.player.brain.player) > 0}
            .filter{ case->case.plan.player == speaker}

        if(dealToOffer != null){
            return OfferDeal(dealToOffer)
        }else if(thingToSuggest.isNotEmpty()){
            return SuggestAction(thingToSuggest[0].plan.actions[0])
        }
        return Approve()
    }

}