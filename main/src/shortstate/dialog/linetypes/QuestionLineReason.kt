package shortstate.dialog.linetypes

import game.action.Action
import shortstate.dialog.Line
import shortstate.dialog.LineBlock

class QuestionLineReason: Line {

    var line: Line? = null

    constructor(line: Line?){
        this.line = line
    }

    override fun tooltipName(): String {
        return "why?"
    }

    override fun symbolicForm(): List<LineBlock> {
        var retval = mutableListOf(LineBlock("QUESTION:"), LineBlock(if(line == null) "Line:___________" else "Line: "+line!!.symbolicForm().toString()))
        if(line == null){
            retval.add(LineBlock("Line:___________"))
        } else {
            retval.addAll(line!!.symbolicForm())
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
        return listOf()
    }
}