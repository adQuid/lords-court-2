package shortstate.dialog.linetypes

import game.action.Effect
import shortstate.Conversation
import shortstate.dialog.Line
import shortstate.dialog.LineBlock

class CiteEffect: Line {
    var effects: List<Effect>? = null

    constructor(effects: List<Effect>?){
        this.effects = effects
    }

    override fun tooltipName(): String {
        return "Give Reason"
    }

    override fun symbolicForm(): List<LineBlock> {
        var retval = mutableListOf(LineBlock("CITE:"))
        if(effects == null){
            retval.add(LineBlock("Effect:___________"))
        } else {
            retval.addAll(effects!!.map{ effect -> LineBlock("Effect: ${effect.toString()}")})
        }

        return retval
    }

    override fun fullTextForm(): String {
        return "I have some very good reasons: " + effects!!.map { effect -> effect.describe() }
    }

    override fun validToSend(): Boolean {
        return effects != null
    }

    override fun possibleReplies(): List<Line> {
        return listOf()
    }

    override fun specialEffect(conversation: Conversation) {
        //No special effects
    }
}