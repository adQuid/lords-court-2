package shortstate.dialog.linetypes

import aibrain.ConversationBrain
import shortstate.Conversation
import shortstate.dialog.Line
import shortstate.dialog.LineBlock
import game.GameCharacter
import game.Game
import game.Writ
import main.UIGlobals
import shortstate.ShortStateCharacter
import shortstate.ShortStateGame
import shortstate.dialog.GlobalLineTypeFactory
import shortstate.room.Room
import ui.specialdisplayables.selectionmodal.SelectionModal
import ui.specialdisplayables.selectionmodal.Tab

class OfferWrit: Line {

    override val type: String
        get() = GlobalLineTypeFactory.OFFER_WRIT_TYPE_NAME
    var writ: Writ?

    constructor(writ: Writ?){
        this.writ = writ
    }

    constructor(saveString: Map<String, Any>, game: Game){
        writ = Writ(saveString["writ"] as Map<String, Any>, game)
    }

    override fun tooltipName(): String {
         return "Offer Writ"
    }

    override fun symbolicForm(context: ShortStateGame, speaker: ShortStateCharacter, target: ShortStateCharacter): List<LineBlock> {
        val dealBlock = if(writ==null){
            LineBlock("Writ: [SELECT]", {perspective -> UIGlobals.focusOn(
                SelectionModal("Select Writ",
                    listOf(
                    Tab(
                        "Writs",
                        perspective.player.writs.filter { !it.signatories.contains(target.player) })
                ), { selectedWrit -> this.writ = selectedWrit; UIGlobals.defocus() }
                )
            )})
        } else {
            LineBlock("Writ: ${writ.toString()}", {perspective ->  UIGlobals.focusOn(writ!!.deal)})
        }
        return listOf(LineBlock("OFFER:"), dealBlock)
    }

    override fun fullTextForm(context: ShortStateGame, speaker: ShortStateCharacter, target: ShortStateCharacter): String {
        if(writ != null){
            return "Would you like to sign this?"
        } else {
            return "Would you like to sign..."
        }
    }

    override fun tooltipDescription(): String {
        return "Ask character to sign a writ. Once everyone in a writ has signed, the writ can be enacted in the throne room."
    }

    override fun specialSaveString(): Map<String, Any> {
        return hashMapOf(
            "writ" to writ!!.saveString()
        )
    }

    override fun validToSend(): Boolean {
        return writ != null
    }

    override fun canChangeTopic(): Boolean {
        return false
    }

    override fun possibleReplies(perspective: ShortStateCharacter, other: ShortStateCharacter, game: Game): List<Line> {
        if(writ!!.deal.actions.filter{entry -> entry.value.filter { !it.isLegal(game, entry.key) }.isNotEmpty()}.isEmpty()){
            return listOf(AcceptWrit(writ!!), RejectDeal(writ!!.deal))
        } else {
            return listOf(RejectDeal(writ!!.deal), SimpleLine("I won't be able to carry that out"))
        }
    }

    override fun AIResponseFunction(brain: ConversationBrain, speaker: ShortStateCharacter, game: Game): Line {
        if(writ!!.deal.actions.filter{entry -> entry.value.filter { !it.isLegal(game, entry.key) }.isNotEmpty()}.isEmpty()){
            if(brain.shortCharacter.player.brain.dealValueToMe(writ!!.deal) > 0){
                return AcceptWrit(writ!!)
            } else {
                return RejectDeal(writ!!.deal)
            }
        } else {
            return SimpleLine("I won't be able to carry that out")
        }
    }
}