package shortstate.dialog.linetypes

import game.action.Action
import shortstate.dialog.Line
import shortstate.dialog.LineBlock

class QuestionSuggestion: Line {

    var line: SuggestAction? = null

    constructor(line: SuggestAction?){
        this.line = line
    }

    override fun tooltipName(): String {
        return "why?"
    }

    override fun symbolicForm(): List<LineBlock> {
        var retval = mutableListOf(LineBlock("QUESTION:"))
        if(line == null){
            retval.add(LineBlock("Line:___________"))
        } else {
            retval.addAll(line!!.symbolicForm().map{block -> block.tab()})
        }
        return retval
    }

    override fun fullTextForm(): String {
        return "Why would you say that?"
    }

    override fun validToSend(): Boolean {
        return line != null
    }

    override fun possibleReplies(): List<Line> {
        return listOf(CiteEffect(null))
    }
}