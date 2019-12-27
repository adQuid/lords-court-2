package shortstate.dialog.linetypes

import aibrain.Deal
import shortstate.Conversation
import shortstate.dialog.Line
import shortstate.dialog.LineBlock
import game.Character

class OfferDeal: Line {

    var deal: Deal? = null

    constructor(deal: Deal?){
        this.deal = deal
    }

    override fun tooltipName(): String {
        return "Suggest Action"
    }

    override fun symbolicForm(): List<LineBlock> {
        return listOf(LineBlock("OFFER:"), LineBlock(if(deal == null) "Deal:___________" else "Deal: "+deal.toString()))
    }

    override fun fullTextForm(speaker: Character, target: Character): String {
        if(deal != null){
            return "I would like to offer a deal: ${deal!!.dialogText(speaker, target)}"
        } else {
            return "I would like to offer a deal: _________"
        }
    }

    override fun validToSend(): Boolean {
        return deal != null
    }

    override fun possibleReplies(): List<Line> {
        return listOf()
    }

    override fun specialEffect(conversation: Conversation) {
        //No special effects
    }
}