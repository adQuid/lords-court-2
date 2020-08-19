package shortstate.dialog.linetypes

import aibrain.ConversationBrain
import shortstate.Conversation
import shortstate.dialog.Line
import shortstate.dialog.LineBlock
import game.GameCharacter
import game.Game
import shortstate.ShortStateCharacter
import shortstate.ShortStateGame
import shortstate.dialog.GlobalLineTypeFactory
import shortstate.room.Room

class Disapprove: Line {

    override val type: String
        get() = GlobalLineTypeFactory.DISAPPROVE_TYPE_NAME

    constructor(){

    }

    override fun tooltipName(): String {
        return "Disapprove"
    }

    override fun symbolicForm(context: ShortStateGame, speaker: ShortStateCharacter, target: ShortStateCharacter): List<LineBlock> {
        return listOf(LineBlock("Disapprove"))
    }

    override fun fullTextForm(context: ShortStateGame, speaker: ShortStateCharacter, target: ShortStateCharacter): String {
        return "I don't like this."
    }

    override fun tooltipDescription(): String {
        return "Disapprove of this."
    }

    override fun specialSaveString(): Map<String, Any> {
        return hashMapOf(GlobalLineTypeFactory.TYPE_NAME to "Disapprove")
    }

    override fun validToSend(): Boolean {
        return true
    }

    override fun canChangeTopic(): Boolean {
        return true
    }

    override fun possibleReplies(perspective: ShortStateCharacter, other: ShortStateCharacter, game: Game): List<Line> {
        return listOf()
    }

    override fun AIResponseFunction(brain: ConversationBrain, speaker: ShortStateCharacter, game: Game): Line {
        return brain.startConversation(speaker, game)
    }
}