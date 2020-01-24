package shortstate.dialog.linetypes

import aibrain.ConversationBrain
import aibrain.Deal
import shortstate.Conversation
import shortstate.dialog.Line
import shortstate.dialog.LineBlock
import game.GameCharacter
import game.Game
import shortstate.dialog.GlobalLineTypeFactory

class RejectDeal: Line {

    override val type: String
        get() = GlobalLineTypeFactory.REJECT_DEAL_TYPE_NAME
    var deal: Deal? = null

    constructor(deal: Deal?){
        this.deal = deal
    }

    constructor(saveString: Map<String, Any>, game: Game){
        deal = Deal(saveString["deal"] as Map<String, Any>, game)
    }

    override fun tooltipName(): String {
        return "Reject Deal"
    }

    override fun symbolicForm(): List<LineBlock> {
        return listOf(LineBlock("REJECT"))
    }

    override fun fullTextForm(speaker: GameCharacter, target: GameCharacter): String {
        return "no thanks"
    }

    override fun specialSaveString(): Map<String, Any> {
        return hashMapOf(
            GlobalLineTypeFactory.TYPE_NAME to "RejectDeal",
            "deal" to deal!!.saveString()
        )
    }

    override fun validToSend(): Boolean {
        return deal != null
    }

    override fun possibleReplies(): List<Line> {
        return listOf()
    }

    override fun specialEffect(conversation: Conversation) {

    }

    override fun AIResponseFunction(brain: ConversationBrain, speaker: GameCharacter, game: Game): Line {
        return Disapprove()
    }
}