package dialog.linetypes

import action.Action
import dialog.Line
import dialog.LineBlock

class Announcement: Line {

    var action: Action? = null

    constructor(){

    }

    constructor(action: Action){
        this.action = action
    }

    override fun fullTextForm(): String {
        return "I will do a thing by the end of the turn. Just wanted to let you know."
    }

    override fun symbolicForm(): List<LineBlock> {
       return listOf(LineBlock("ANNOUNCE:"), LineBlock("Action:___________"))
    }
}