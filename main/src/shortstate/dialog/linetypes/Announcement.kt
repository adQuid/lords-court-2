package shortstate.dialog.linetypes

import game.action.Action
import shortstate.dialog.Line
import shortstate.dialog.LineBlock

class Announcement: Line, HasAction {

    var action: Action? = null

    constructor(action: Action?){
        this.action = action
    }

    override fun tooltipName(): String {
        return "Declare Action"
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

    override fun possibleReplies(): List<Line> {
        return listOf(Approve(), Disapprove())
    }

    override fun mySetAction(action: Action) {
        this.action = action
    }

    override fun myGetAction(): Action? {
        return this.action
    }
}