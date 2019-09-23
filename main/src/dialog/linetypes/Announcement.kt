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
        return listOf(LineBlock("ANNOUNCE:"), LineBlock("Action:___________"))
    }

    override fun fullTextForm(): String {
        return "I will do a thing by the end of the turn. Just wanted to let you know."
    }

}