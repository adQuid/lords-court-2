package dialog.linetypes

import action.Action
import dialog.Line
import dialog.LineBlock

class Announcement: Line {

    var action: Action? = null

    constructor(action: Action?){
        this.action = action
    }

    override fun symbolicForm(): List<LineBlock> {
        return listOf(LineBlock("ANNOUNCE:"), LineBlock(if(action == null) "Action:___________" else "Action: "+action.toString()))
    }

    override fun fullTextForm(): String {
        return "I will "+action?.toString()+" by the end of the turn. Just wanted to let you know."
    }

    override fun validToSend(): Boolean {
        return action != null
    }
}