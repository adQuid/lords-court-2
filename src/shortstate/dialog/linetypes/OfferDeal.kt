package shortstate.dialog.linetypes

import aibrain.ConversationBrain
import aibrain.Deal
import aibrain.FinishedDeal
import shortstate.Conversation
import shortstate.dialog.Line
import shortstate.dialog.LineBlock
import game.GameCharacter
import game.Game
import main.Controller
import shortstate.dialog.GlobalLineTypeFactory

class OfferDeal: Line {

    override val type: String
        get() = GlobalLineTypeFactory.OFFER_DEAL_TYPE_NAME
    var deal: Deal? = null

    constructor(deal: Deal?){
        this.deal = deal
    }

    constructor(saveString: Map<String, Any>, game: Game){
        deal = FinishedDeal(saveString["deal"] as Map<String, Any>, game)
    }

    override fun tooltipName(): String {
        if(deal == null){
            return "Offer Deal"
        } else {
            return "Offer Counter-proposal"
        }
    }

    override fun symbolicForm(): List<LineBlock> {
        val dealBlock = if(deal==null){
            LineBlock("Deal:___________")
        } else {
            LineBlock("Deal: [CLICK FOR DETAILS]", { Controller.singleton!!.GUI!!.focusOn(deal)})
        }
        return listOf(LineBlock("OFFER:"), dealBlock)
    }

    override fun fullTextForm(speaker: GameCharacter, target: GameCharacter): String {
        if(deal != null){
            return "I would like to offer a deal: ${deal!!.dialogText(speaker, target)}"
        } else {
            return "I would like to offer a deal: _________"
        }
    }

    override fun specialSaveString(): Map<String, Any> {
        return hashMapOf(
            GlobalLineTypeFactory.TYPE_NAME to "OfferDeal",
            "deal" to (deal!! as FinishedDeal).saveString()
        )
    }

    override fun validToSend(): Boolean {
        return deal != null
    }

    override fun possibleReplies(): List<Line> {
        val newDeal = deal!!.toFinishedDeal()
        return listOf(AcceptDeal(newDeal), RejectDeal(newDeal), OfferDeal(newDeal.toUnfinishedDeal()))
    }

    override fun specialEffect(conversation: Conversation) {
        //No special effects
    }

    override fun AIResponseFunction(brain: ConversationBrain, speaker: GameCharacter, game: Game): Line {
        return AcceptDeal(deal!!.toFinishedDeal())
    }
}