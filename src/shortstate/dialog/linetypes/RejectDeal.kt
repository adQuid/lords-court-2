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
import shortstate.ShortStateGame
import shortstate.dialog.GlobalLineTypeFactory
import shortstate.room.Room

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

    override fun symbolicForm(context: ShortStateGame, speaker: ShortStateCharacter, target: ShortStateCharacter): List<LineBlock> {
        return listOf(LineBlock("REJECT"), LineBlock("Deal: [CLICK FOR DETAILS]", { UIGlobals.focusOn(deal)}))
    }

    override fun fullTextForm(context: ShortStateGame, speaker: ShortStateCharacter, target: ShortStateCharacter): String {
        return "no thanks"
    }

    override fun tooltipDescription(): String {
        return "Express disinterest in this deal."
    }

    override fun specialSaveString(): Map<String, Any> {
        return hashMapOf(
            "deal" to deal!!.saveString()
        )
    }

    override fun validToSend(): Boolean {
        return deal != null
    }

    override fun canChangeTopic(): Boolean {
        return false
    }

    override fun possibleReplies(perspective: ShortStateCharacter, other: ShortStateCharacter, game: Game): List<Line> {
        return listOf()
    }

    override fun AIResponseFunction(brain: ConversationBrain, speaker: ShortStateCharacter, game: Game): Line {
        return Disapprove()
    }
}