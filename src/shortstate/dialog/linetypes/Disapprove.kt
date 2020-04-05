package shortstate.dialog.linetypes

import aibrain.ConversationBrain
import shortstate.Conversation
import shortstate.dialog.Line
import shortstate.dialog.LineBlock
import game.GameCharacter
import game.Game
import shortstate.ShortStateCharacter
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

    override fun symbolicForm(speaker: ShortStateCharacter, target: ShortStateCharacter): List<LineBlock> {
        return listOf(LineBlock("Disapprove"))
    }

    override fun fullTextForm(sspeaker: ShortStateCharacter, target: ShortStateCharacter): String {
        return "I don't like this."
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

    override fun possibleReplies(perspective: ShortStateCharacter): List<Line> {
        return listOf(Farewell())
    }

    override fun specialEffect(room: Room, conversation: Conversation, speaker: ShortStateCharacter) {
        //No special effects
    }

    override fun AIResponseFunction(brain: ConversationBrain, speaker: GameCharacter, game: Game): Line {
        return brain.startConversation(speaker, game)
    }
}