package shortstate.dialog.linetypes

import game.action.Action
import shortstate.dialog.Line
import shortstate.dialog.LineBlock

class RequestAction: Line {

    var action: Action? = null

    constructor(action: Action?){
        this.action = action
    }

    override fun tooltipName(): String {
        return "Request"
    }

    override fun symbolicForm(): List<LineBlock> {
        return listOf(LineBlock("REQUEST:"), LineBlock(if(action == null) "Action:___________" else "Action: "+action.toString()))
    }

    override fun fullTextForm(): String {
        return "Yo, could you "+action?.toString()+" this turn?"
    }

    override fun validToSend(): Boolean {
        return action != null
    }

    override fun possibleReplies(): List<Line> {
        return listOf(Announcement(action))
    }
}