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
import game.Writ
import game.action.Action
import main.Controller
import main.UIGlobals
import shortstate.ShortStateCharacter
import shortstate.dialog.GlobalLineTypeFactory

class OfferWrit: Line {

    override val type: String
        get() = GlobalLineTypeFactory.OFFER_WRIT_TYPE_NAME
    var writ: Writ?

    constructor(writ: Writ?){
        this.writ = writ
    }

    constructor(saveString: Map<String, Any>, game: Game){
        writ = Writ(saveString["writ"] as Map<String, Any>, game)
    }

    override fun tooltipName(): String {
         return "Offer Writ"
    }

    override fun symbolicForm(speaker: ShortStateCharacter, target: ShortStateCharacter): List<LineBlock> {
        val dealBlock = if(writ==null){
            LineBlock("Writ: [SELECT]")
        } else {
            LineBlock("Writ: ${writ.toString()}")
        }
        return listOf(LineBlock("OFFER:"), dealBlock)
    }

    override fun fullTextForm(speaker: ShortStateCharacter, target: ShortStateCharacter): String {
        if(writ != null){
            return "Would you like to sign this?"
        } else {
            return "Would you like to sign..."
        }
    }

    override fun specialSaveString(): Map<String, Any> {
        return hashMapOf(
            "writ" to writ!!.saveString()
        )
    }

    override fun validToSend(): Boolean {
        return writ != null
    }

    override fun possibleReplies(): List<Line> {
        return listOf(Approve())
    }

    override fun specialEffect(conversation: Conversation) {
        //No special effects
    }

    override fun AIResponseFunction(brain: ConversationBrain, speaker: GameCharacter, game: Game): Line {
        return Approve()
    }
}