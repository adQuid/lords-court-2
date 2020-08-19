package shortstate.dialog.linetypes

import aibrain.ConversationBrain
import aibrain.Deal
import aibrain.FinishedDeal
import aibrain.UnfinishedDeal
import shortstate.Conversation
import shortstate.dialog.Line
import shortstate.dialog.LineBlock
import game.GameCharacter
import game.Game
import game.action.Action
import main.Controller
import main.UIGlobals
import shortstate.ShortStateCharacter
import shortstate.ShortStateGame
import shortstate.dialog.GlobalLineTypeFactory
import shortstate.room.Room

class OfferDeal: Line {

    override val type: String
        get() = GlobalLineTypeFactory.OFFER_DEAL_TYPE_NAME
    var deal: Deal

    constructor(deal: Deal){
        this.deal = deal
    }

    constructor(saveString: Map<String, Any>, game: Game){
        deal = FinishedDeal(saveString["deal"] as Map<String, Any>, game)
    }

    override fun tooltipName(): String {
        if(!validToSend()){
            return "Offer Deal"
        } else {
            return "Offer Counter-proposal"
        }
    }

    override fun symbolicForm(context: ShortStateGame, speaker: ShortStateCharacter, target: ShortStateCharacter): List<LineBlock> {
        val dealBlock = if(deal==null){
            LineBlock("Deal:___________")
        } else {
            LineBlock("Deal: [CLICK FOR DETAILS]", { UIGlobals.focusOn(deal)})
        }
        return listOf(LineBlock("OFFER:"), dealBlock)
    }

    override fun fullTextForm(context: ShortStateGame, speaker: ShortStateCharacter, target: ShortStateCharacter): String {
        if(deal != null){
            return "I would like to offer a deal: ${deal!!.dialogText(speaker.player, target.player)}"
        } else {
            return "I would like to offer a deal: _________"
        }
    }

    override fun tooltipDescription(): String {
        return "Ask if a deal would be in this character's interest. Agreeing doesn't guarentee the actions will really happen."
    }

    override fun specialSaveString(): Map<String, Any> {
        return hashMapOf(
            "deal" to (deal!!.toFinishedDeal()).saveString()
        )
    }

    override fun validToSend(): Boolean {
        return deal.theActions().values.fold(0, {acc: Int, list: Set<Action> ->  acc+list.size}) > 0
    }

    override fun canChangeTopic(): Boolean {
        return false
    }

    override fun possibleReplies(perspective: ShortStateCharacter, other: ShortStateCharacter, game: Game): List<Line> {
        if(deal!!.theActions().filter{entry -> entry.value.filter { !it.isLegal(game, entry.key) }.isNotEmpty()}.isEmpty()){
            val newDeal = deal!!.toFinishedDeal()
            return listOf(AcceptDeal(newDeal), RejectDeal(newDeal), OfferDeal(newDeal.toUnfinishedDeal()), QuestionOffer(this))
        } else {
            return listOf(RejectDeal(deal!!.toFinishedDeal()), SimpleLine("I won't be able to carry that out"))
        }
    }

    override fun specialEffect(room: Room, shortGame: ShortStateGame, speaker: ShortStateCharacter) {
        shortGame.shortGameScene!!.conversation!!.participants().forEach { it.convoBrain.putOrAddDealToMemory(shortGame.shortGameScene!!.conversation!!.otherParticipant(it).player, deal) }
    }

    override fun AIResponseFunction(brain: ConversationBrain, speaker: ShortStateCharacter, game: Game): Line {
        if(deal!!.theActions().filter{entry -> entry.value.filter { !it.isLegal(game, entry.key) }.isNotEmpty()}.isEmpty()){
            val value = brain.shortCharacter.player.brain.dealValueToMe(deal)
            if(value > 0){
                return AcceptDeal(deal.toFinishedDeal())
            } else if(value < 0){
                return RejectDeal(deal.toFinishedDeal())
            } else {
                return SimpleLine("Whatever you want, my lord")
            }
        } else {
            return SimpleLine("I won't be able to carry that out")
        }
    }
}