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
        return listOf(Approve(), Disapprove())
    }

    override fun specialEffect(room: Room, shortGame: ShortStateGame, speaker: ShortStateCharacter) {
        shortGame.shortGameScene!!.conversation!!.participants().forEach { it.convoBrain.putOrAddDealToMemory(shortGame.shortGameScene!!.conversation!!.otherParticipant(it).player, deal) }
    }

    override fun AIResponseFunction(brain: ConversationBrain, speaker: ShortStateCharacter, game: Game): Line {
        //TODO: Move this division to somewhere lower
        val score = brain.shortCharacter.player.brain.dealScoreToCharacter(deal, speaker.player).dividedBy(brain.shortCharacter.player.brain.lastCasesOfConcern!!.filter { it.plan.player != speaker.player }.size.toDouble())
        val badFactors = score.components().filter{it.value < 0}.sortedBy { it.value }.map { it.description() }
        val goodFactors = score.components().filter{it.value > 0}.sortedByDescending { it.value }.map { it.description() }

        if(score.value() > 0){
            return SimpleLine(goodDealTemplate(goodFactors, badFactors))
        } else if(score.value() < 0){
            return SimpleLine(badDealTemplate(goodFactors, badFactors))
        } else {
            if(badFactors.size > 0 && goodFactors.size > 0){
                return SimpleLine(mixedDealTemplate(goodFactors, badFactors))
            } else {
                return SimpleLine(pointlessDealTemplate(goodFactors, badFactors))
            }
        }
    }

    private fun goodDealTemplate(goodFactors: List<String>, badFactors: List<String>): String{
        return "I think this would be a good idea for you, and here's why: ${goodFactors.joinToString(", ")}"
    }

    private fun badDealTemplate(goodFactors: List<String>, badFactors: List<String>): String{
        return "I think this would be a bad idea for you, and here's why: ${badFactors.joinToString(", ")}"
    }

    private fun mixedDealTemplate(goodFactors: List<String>, badFactors: List<String>): String{
        return "On the one hand I do see that ${goodFactors.joinToString(", and ")}, but keep in mind that, ${badFactors.joinToString(", and")}. These seem about equivalent to me."
    }

    private fun pointlessDealTemplate(goodFactors: List<String>, badFactors: List<String>): String{
        return "I don't see how this will make a difference"
    }
}