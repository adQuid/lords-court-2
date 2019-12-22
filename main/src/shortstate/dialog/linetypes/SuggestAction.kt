package shortstate.dialog.linetypes

import game.action.Action
import shortstate.Conversation
import shortstate.dialog.Line
import shortstate.dialog.LineBlock

class SuggestAction: Line {

    var action: Action? = null

    constructor(action: Action?){
        this.action = action
    }

    override fun tooltipName(): String {
        return "Suggest Action"
    }

    override fun symbolicForm(): List<LineBlock> {
        return listOf(LineBlock("REQUEST:"), LineBlock(if(action == null) "Action:___________" else "Action: "+action.toString()))
    }

    override fun fullTextForm(): String {
        return "I think you should "+action?.toString()+" this turn."
    }

    override fun validToSend(): Boolean {
        return action != null
    }

    override fun possibleReplies(): List<Line> {
        return listOf(Announcement(action), QuestionSuggestion(this))
    }

    override fun specialEffect(conversation: Conversation) {
        //No special effects
    }
}