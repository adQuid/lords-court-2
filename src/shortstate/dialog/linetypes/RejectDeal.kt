package shortstate.dialog.linetypes

import aibrain.ConversationBrain
import aibrain.Deal
import aibrain.FinishedDeal
import shortstate.Conversation
import shortstate.dialog.Line
import shortstate.dialog.LineBlock
import game.GameCharacter
import game.Game
import main.UIGlobals
import shortstate.ShortStateCharacter
import shortstate.dialog.GlobalLineTypeFactory

class RejectDeal: Line {

    override val type: String
        get() = GlobalLineTypeFactory.REJECT_DEAL_TYPE_NAME
    var deal: FinishedDeal? = null

    constructor(deal: FinishedDeal?){
        this.deal = deal
    }

    constructor(saveString: Map<String, Any>, game: Game){
        deal = FinishedDeal(saveString["deal"] as Map<String, Any>, game)
    }

    override fun tooltipName(): String {
        return "Reject Deal"
    }

    override fun symbolicForm(speaker: ShortStateCharacter, target: ShortStateCharacter): List<LineBlock> {
        return listOf(LineBlock("REJECT"), LineBlock("Deal: [CLICK FOR DETAILS]", { UIGlobals.focusOn(deal)}))
    }

    override fun fullTextForm(speaker: ShortStateCharacter, target: ShortStateCharacter): String {
        return "no thanks"
    }

    override fun specialSaveString(): Map<String, Any> {
        return hashMapOf(
            "deal" to deal!!.saveString()
        )
    }

    override fun validToSend(): Boolean {
        return deal != null
    }

    override fun possibleReplies(perspective: ShortStateCharacter): List<Line> {
        return listOf()
    }

    override fun specialEffect(conversation: Conversation, speaker: ShortStateCharacter) {

    }

    override fun AIResponseFunction(brain: ConversationBrain, speaker: GameCharacter, game: Game): Line {
        return Disapprove()
    }
}