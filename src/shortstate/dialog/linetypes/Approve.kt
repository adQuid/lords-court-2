package shortstate.dialog.linetypes

import aibrain.ConversationBrain
import shortstate.Conversation
import shortstate.dialog.Line
import shortstate.dialog.LineBlock
import game.GameCharacter
import game.Game
import shortstate.dialog.GlobalLineTypeFactory

class Approve: Line {

    override val type: String
        get() = GlobalLineTypeFactory.APPROVE_TYPE_NAME

    constructor() {

    }

    override fun tooltipName(): String {
        return "Approve"
    }

    override fun symbolicForm(): List<LineBlock> {
        return listOf(LineBlock("Approve"))
    }

    override fun fullTextForm(speaker: GameCharacter, target: GameCharacter): String {
        return "YAASSSSS"
    }

    override fun specialSaveString(): Map<String, Any> {
        return hashMapOf(GlobalLineTypeFactory.TYPE_NAME to "Approve")
    }

    override fun validToSend(): Boolean {
        return true
    }

    override fun possibleReplies(): List<Line> {
        return listOf()
    }

    override fun specialEffect(conversation: Conversation) {
        //No special effects
    }

    override fun AIResponseFunction(brain: ConversationBrain, speaker: GameCharacter, game: Game): Line {
        return Approve()
    }
}