package shortstate.dialog.linetypes

import aibrain.ConversationBrain
import aibrain.Deal
import aibrain.FinishedDeal
import shortstate.Conversation
import shortstate.dialog.Line
import shortstate.dialog.LineBlock
import game.GameCharacter
import game.Game
import shortstate.ShortStateCharacter
import shortstate.dialog.GlobalLineTypeFactory

class AcceptDeal: Line {

    override val type: String
        get() = GlobalLineTypeFactory.ACCEPT_DEAL_TYPE_NAME

    var deal: FinishedDeal? = null

    constructor(deal: FinishedDeal?){
        this.deal = deal
    }

    constructor(saveString: Map<String, Any?>, game: Game){
        if(saveString["DEAL"] != null){
            deal = FinishedDeal(saveString["DEAL"] as Map<String, Any>, game)
        }
    }

    override fun tooltipName(): String {
        return "Accept Deal"
    }

    override fun symbolicForm(speaker: ShortStateCharacter, target: ShortStateCharacter): List<LineBlock> {
        return listOf(LineBlock("ACCEPT"))
    }

    override fun fullTextForm(speaker: ShortStateCharacter, target: ShortStateCharacter): String {
        return "sounds reasonable"
    }

    override fun specialSaveString(): Map<String, Any> {
        return hashMapOf(
            "DEAL" to deal!!.saveString()
        )
    }

    override fun validToSend(): Boolean {
        return deal != null
    }

    override fun possibleReplies(): List<Line> {
        return listOf()
    }

    override fun specialEffect(conversation: Conversation) {
        conversation.initiator.player.acceptedDeals.add(deal!!)
        conversation.target.player.acceptedDeals.add(deal!!)
    }

    override fun AIResponseFunction(brain: ConversationBrain, speaker: GameCharacter, game: Game): Line {
        return Approve()
    }
}