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

class RequestAdviceForDeal: Line {

    override val type: String
        get() = GlobalLineTypeFactory.REQUEST_DEAL_ADVICE_TYPE_NAME
    var deal: Deal

    constructor(deal: Deal){
        this.deal = deal
    }

    constructor(saveString: Map<String, Any>, game: Game){
        deal = FinishedDeal(saveString["deal"] as Map<String, Any>, game)
    }

    override fun tooltipName(): String {
        if(!validToSend()){
            return "Request Advice on Deal"
        } else {
            return "Offer Alternative"
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
            return "What would you think about this plan: ${deal!!.dialogText(speaker.player, target.player)}"
        } else {
            return "What would you think about this plan: _________"
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
        return listOf(Approve(), Disapprove())
    }

    override fun specialEffect(room: Room, conversation: Conversation, speaker: ShortStateCharacter) {
        conversation.participants().forEach { it.convoBrain.putOrAddDealToMemory(conversation.otherParticipant(it).player, deal) }
    }

    override fun AIResponseFunction(brain: ConversationBrain, speaker: GameCharacter, game: Game): Line {
        val value = brain.shortCharacter.player.brain.dealValueToCharacter(deal, speaker)
        if(value > 0){
            return SimpleLine("Seems like a good idea to me")
        } else if(value < 0){
            return Disapprove()
        } else {
            return SimpleLine("I don't really think this makes a difference")
        }
    }
}