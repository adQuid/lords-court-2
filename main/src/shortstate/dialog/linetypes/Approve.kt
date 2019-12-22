package shortstate.dialog.linetypes

import shortstate.Conversation
import shortstate.dialog.Line
import shortstate.dialog.LineBlock

class Approve: Line {

    constructor(){

    }

    override fun tooltipName(): String {
        return "Approve"
    }

    override fun symbolicForm(): List<LineBlock> {
        return listOf(LineBlock("Approve"))
    }

    override fun fullTextForm(): String {
        return "YAASSSSS"
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
}