package shortstate.dialog.linetypes

import shortstate.Conversation
import shortstate.dialog.Line
import shortstate.dialog.LineBlock
import game.Character

class Disapprove: Line {

    constructor(){

    }

    override fun tooltipName(): String {
        return "Disapprove"
    }

    override fun symbolicForm(): List<LineBlock> {
        return listOf(LineBlock("Disapprove"))
    }

    override fun fullTextForm(speaker: Character, target: Character): String {
        return "NOOO!!!"
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