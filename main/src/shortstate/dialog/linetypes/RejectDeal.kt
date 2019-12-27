package shortstate.dialog.linetypes

import aibrain.ConversationBrain
import aibrain.Deal
import shortstate.Conversation
import shortstate.dialog.Line
import shortstate.dialog.LineBlock
import game.Character
import game.Game

class RejectDeal: Line {

    var deal: Deal? = null

    constructor(deal: Deal?){
        this.deal = deal
    }

    override fun tooltipName(): String {
        return "Reject Deal"
    }

    override fun symbolicForm(): List<LineBlock> {
        return listOf(LineBlock("REJECT"))
    }

    override fun fullTextForm(speaker: Character, target: Character): String {
        return "no thanks"
    }

    override fun validToSend(): Boolean {
        return deal != null
    }

    override fun possibleReplies(): List<Line> {
        return listOf()
    }

    override fun specialEffect(conversation: Conversation) {

    }

    override fun AIResponseFunction(brain: ConversationBrain, speaker: Character, game: Game): Line {
        return Disapprove()
    }
}