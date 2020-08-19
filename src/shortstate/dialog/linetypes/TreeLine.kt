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

class TreeLine: Line {

    override val type: String
        get() = GlobalLineTypeFactory.TREE_TYPE_NAME

    val text: String
    val children: List<Line>

    constructor(text: String, vararg children: Line) {
        this.text = text
        this.children = children.toList()
    }

    constructor(map: Map<String, Any>, game: Game){
        text = map["txt"] as String
        children = (map["children"] as List<Map<String,Any>>).map { GlobalLineTypeFactory.fromMap(it, game) }
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
        return hashMapOf("txt" to text, "children" to children.map { it.saveString() })
    }

    override fun validToSend(): Boolean {
        return true
    }

    override fun canChangeTopic(): Boolean {
        return false
    }

    override fun possibleReplies(perspective: ShortStateCharacter, other: ShortStateCharacter, game: Game): List<Line> {
        return children
    }

    override fun AIResponseFunction(brain: ConversationBrain, speaker: ShortStateCharacter, game: Game): Line {
        if(children.isNotEmpty()){
            return children.first()
        }
        return brain.startConversation(speaker, game)
    }
}