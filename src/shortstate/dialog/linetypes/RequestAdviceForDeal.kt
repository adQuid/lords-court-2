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

    override fun tooltipDescription(): String {
        return "Ask character if this deal would be a good plan for you."
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
        val newDeal = deal!!.toFinishedDeal()
        return listOf(Approve(), Disapprove())
    }

    override fun specialEffect(room: Room, shortGame: ShortStateGame, speaker: ShortStateCharacter) {
        shortGame.shortGameScene!!.conversation!!.participants().forEach { it.convoBrain.putOrAddDealToMemory(shortGame.shortGameScene!!.conversation!!.otherParticipant(it).player, deal) }
    }

    override fun AIResponseFunction(brain: ConversationBrain, speaker: ShortStateCharacter, game: Game): Line {
        val score = brain.shortCharacter.player.brain.dealScoreToCharacter(deal, speaker.player)
        return SimpleLine(score.components().map { " ${it.name}:${it.value} " }.joinToString(","))
    }
}