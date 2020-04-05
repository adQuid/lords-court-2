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

    override fun symbolicForm(speaker: ShortStateCharacter, target: ShortStateCharacter): List<LineBlock> {
        val dealBlock = if(deal==null){
            LineBlock("Deal:___________")
        } else {
            LineBlock("Deal: [CLICK FOR DETAILS]", { UIGlobals.focusOn(deal)})
        }
        return listOf(LineBlock("OFFER:"), dealBlock)
    }

    override fun fullTextForm(speaker: ShortStateCharacter, target: ShortStateCharacter): String {
        if(deal != null){
            return "I would like to offer a deal: ${deal!!.dialogText(speaker.player, target.player)}"
        } else {
            return "I would like to offer a deal: _________"
        }
    }

    override fun specialSaveString(): Map<String, Any> {
        return hashMapOf(
            "deal" to (deal!! as FinishedDeal).saveString()
        )
    }

    override fun validToSend(): Boolean {
        return deal.theActions().values.fold(0, {acc: Int, list: Set<Action> ->  acc+list.size}) > 0
    }

    override fun canChangeTopic(): Boolean {
        return false
    }

    override fun possibleReplies(perspective: ShortStateCharacter): List<Line> {
        val newDeal = deal!!.toFinishedDeal()
        return listOf(AcceptDeal(newDeal), RejectDeal(newDeal), OfferDeal(newDeal.toUnfinishedDeal()), QuestionOffer(this))
    }

    override fun specialEffect(room: Room, conversation: Conversation, speaker: ShortStateCharacter) {
        conversation.participants().forEach { it.convoBrain.putOrAddDealToMemory(conversation.otherParticipant(it).player, deal) }
    }

    override fun AIResponseFunction(brain: ConversationBrain, speaker: GameCharacter, game: Game): Line {
        if(brain.shortCharacter.player.brain.dealValueToMe(deal) > 0){
            return AcceptDeal(deal.toFinishedDeal())
        } else {
            return QuestionOffer(this)
        }
    }
}