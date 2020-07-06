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

class SimpleLine: Line {

    override val type: String
        get() = GlobalLineTypeFactory.SIMPLE_TYPE_NAME

    val text: String

    constructor(text: String) {
        this.text = text
    }

    constructor(map: Map<String, Any>, game: Game){
        text = map["txt"] as String
    }

    override fun tooltipName(): String {
        return "\"${text}\""
    }

    override fun symbolicForm(context: ShortStateGame, speaker: ShortStateCharacter, target: ShortStateCharacter): List<LineBlock> {
        return listOf(LineBlock("Say:"), LineBlock(text))
    }

    override fun fullTextForm(context: ShortStateGame, speaker: ShortStateCharacter, target: ShortStateCharacter): String {
        return text
    }

    override fun tooltipDescription(): String {
        return ""
    }

    override fun specialSaveString(): Map<String, Any> {
        return hashMapOf("txt" to text)
    }

    override fun validToSend(): Boolean {
        return true
    }

    override fun canChangeTopic(): Boolean {
        return true
    }

    override fun possibleReplies(perspective: ShortStateCharacter): List<Line> {
        return listOf()
    }

    override fun specialEffect(room: Room, conversation: Conversation, speaker: ShortStateCharacter) {
        //No special effects
    }

    override fun AIResponseFunction(brain: ConversationBrain, speaker: GameCharacter, game: Game): Line {
        return brain.startConversation(speaker, game)
    }
}