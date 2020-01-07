package shortstate.dialog.linetypes

import aibrain.ConversationBrain
import aibrain.Deal
import shortstate.Conversation
import shortstate.dialog.Line
import shortstate.dialog.LineBlock
import game.Character
import game.Game

class AcceptDeal: Line {

    var deal: Deal? = null

    constructor(deal: Deal?){
        this.deal = deal
    }

    override fun tooltipName(): String {
        return "Accept Deal"
    }

    override fun symbolicForm(): List<LineBlock> {
        return listOf(LineBlock("ACCEPT"))
    }

    override fun fullTextForm(speaker: Character, target: Character): String {
        return "sounds reasonable"
    }

    override fun saveString(): Map<String, Any> {
        return hashMapOf(
            GlobalLineTypeList.TYPE_NAME to "AcceptDeal",
            "DEAL" to deal!!.saveString()
        )
    }

    override fun validToSend(): Boolean {
        return deal != null
    }

    override fun possibleReplies(): List<Line> {
        return listOf()
    }

    override fun specialEffect(conversation: Conversation) {
        conversation.initiator.player.acceptedDeals.add(deal!!)
        conversation.target.player.acceptedDeals.add(deal!!)
    }

    override fun AIResponseFunction(brain: ConversationBrain, speaker: Character, game: Game): Line {
        return Approve()
    }
}