package shortstate.dialog.linetypes

import aibrain.ConversationBrain
import aibrain.Deal
import aibrain.FinishedDeal
import shortstate.Conversation
import shortstate.dialog.Line
import shortstate.dialog.LineBlock
import game.GameCharacter
import game.Game
import game.Writ
import shortstate.ShortStateCharacter
import shortstate.dialog.GlobalLineTypeFactory

class AcceptWrit: Line {

    override val type: String
        get() = GlobalLineTypeFactory.ACCEPT_DEAL_TYPE_NAME

    lateinit var writ: Writ

    constructor(writ: Writ){
        this.writ = writ
    }

    constructor(saveString: Map<String, Any?>, game: Game){
        if(saveString["writ"] != null){
            writ = Writ(saveString["writ"] as Map<String, Any>, game)
        }
    }

    override fun tooltipName(): String {
        return "Sign Writ"
    }

    override fun symbolicForm(speaker: ShortStateCharacter, target: ShortStateCharacter): List<LineBlock> {
        return listOf(LineBlock("ACCEPT"))
    }

    override fun fullTextForm(speaker: ShortStateCharacter, target: ShortStateCharacter): String {
        return "I'll be happy to sign that"
    }

    override fun specialSaveString(): Map<String, Any> {
        return hashMapOf(
            "writ" to writ!!.saveString()
        )
    }

    override fun validToSend(): Boolean {
        return true
    }

    override fun possibleReplies(): List<Line> {
        return listOf()
    }

    override fun specialEffect(conversation: Conversation, speaker: ShortStateCharacter) {
        writ.signatories.add(speaker.player)
    }

    override fun AIResponseFunction(brain: ConversationBrain, speaker: GameCharacter, game: Game): Line {
        return Approve()
    }
}