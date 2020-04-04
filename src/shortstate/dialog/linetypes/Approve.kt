package shortstate.dialog.linetypes

import aibrain.ConversationBrain
import shortstate.Conversation
import shortstate.dialog.Line
import shortstate.dialog.LineBlock
import game.GameCharacter
import game.Game
import shortstate.ShortStateCharacter
import shortstate.dialog.GlobalLineTypeFactory

class Approve: Line {

    override val type: String
        get() = GlobalLineTypeFactory.APPROVE_TYPE_NAME

    constructor() {

    }

    override fun tooltipName(): String {
        return "Approve"
    }

    override fun symbolicForm(speaker: ShortStateCharacter, target: ShortStateCharacter): List<LineBlock> {
        return listOf(LineBlock("Approve"))
    }

    override fun fullTextForm(speaker: ShortStateCharacter, target: ShortStateCharacter): String {
        return "I like this."
    }

    override fun specialSaveString(): Map<String, Any> {
        return hashMapOf()
    }

    override fun validToSend(): Boolean {
        return true
    }

    override fun possibleReplies(perspective: ShortStateCharacter): List<Line> {
        return listOf()
    }

    override fun specialEffect(conversation: Conversation, speaker: ShortStateCharacter) {
        //No special effects
    }

    override fun AIResponseFunction(brain: ConversationBrain, speaker: GameCharacter, game: Game): Line {
        return brain.startConversation(speaker, game)
    }
}