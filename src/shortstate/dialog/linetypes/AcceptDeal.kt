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
import shortstate.ShortStateGame
import shortstate.dialog.GlobalLineTypeFactory
import shortstate.room.Room

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

    override fun symbolicForm(context: ShortStateGame, speaker: ShortStateCharacter, target: ShortStateCharacter): List<LineBlock> {
        return listOf(LineBlock("ACCEPT"))
    }

    override fun fullTextForm(context: ShortStateGame, speaker: ShortStateCharacter, target: ShortStateCharacter): String {
        return "sounds reasonable"
    }

    override fun tooltipDescription(): String {
        return "Agree to deal. Actions are still not enacted until enacted in a writ."
    }

    override fun specialSaveString(): Map<String, Any> {
        return hashMapOf(
            "DEAL" to deal!!.saveString()
        )
    }

    override fun validToSend(): Boolean {
        return deal != null
    }

    override fun canChangeTopic(): Boolean {
        return false
    }

    override fun possibleReplies(perspective: ShortStateCharacter): List<Line> {
        return listOf()
    }

    override fun specialEffect(room: Room, conversation: Conversation, speaker: ShortStateCharacter) {
        conversation.initiator.player.acceptedDeals.add(deal!!)
        conversation.target.player.acceptedDeals.add(deal!!)
    }

    override fun AIResponseFunction(brain: ConversationBrain, speaker: ShortStateCharacter, game: Game): Line {
        return SimpleLine("Good to hear")
    }
}