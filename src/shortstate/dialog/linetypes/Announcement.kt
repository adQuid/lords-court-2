package shortstate.dialog.linetypes

import aibrain.ConversationBrain
import aibrain.FinishedDeal
import game.action.Action
import shortstate.Conversation
import shortstate.dialog.Line
import shortstate.dialog.LineBlock
import shortstate.dialog.linetypes.traits.HasAction
import game.GameCharacter
import game.Game
import main.UIGlobals
import shortstate.ShortStateCharacter
import shortstate.dialog.GlobalLineTypeFactory
import ui.specialdisplayables.selectionmodal.SelectionModal
import ui.specialdisplayables.selectionmodal.Tab
import game.action.GlobalActionTypeFactory
import shortstate.ShortStateGame
import shortstate.room.Room

class Announcement: Line, HasAction {

    override val type: String
        get() = GlobalLineTypeFactory.ANNOUNCEMENT_TYPE_NAME

    var action: Action? = null

    constructor(action: Action?){
        this.action = action
    }

    constructor(saveString: Map<String, Any?>, game: Game){
        if(saveString["ACTION"] != null){
            action = GlobalActionTypeFactory.fromMap(saveString["ACTION"] as Map<String, Any>, game)
        }
    }

    override fun tooltipName(): String {
        return "Declare Action"
    }

    override fun symbolicForm(context: ShortStateGame, speaker: ShortStateCharacter, target: ShortStateCharacter): List<LineBlock> {
        return listOf(LineBlock("ANNOUNCE:"), LineBlock(if(action == null) "SELECT ACTION" else "Action: "+action.toString(),
            null, {perspective -> UIGlobals.focusOn(
                SelectionModal( "Annouce...",
                    listOf(
                        Tab(
                            "Basic Actions",
                            speaker.player.actionsReguarding(listOf(target.player))
                        )
                    ),
                    { action ->
                        mySetAction(action); UIGlobals.defocus();
                    })
            )}))
    }

    override fun fullTextForm(context: ShortStateGame, speaker: ShortStateCharacter, target: ShortStateCharacter): String {
        return "I will "+if(action != null) action.toString() else "______"+" by the end of the turn. Just wanted to let you know."
    }

    override fun tooltipDescription(): String {
        return "Declare that you intend to do an action, either to inform others or just gauge reaction."
    }

    override fun specialSaveString(): Map<String, Any> {
        return hashMapOf(
            "ACTION" to action!!.saveString()
        )
    }

    override fun validToSend(): Boolean {
        return action != null
    }

    override fun canChangeTopic(): Boolean {
        return false
    }

    override fun possibleReplies(perspective: ShortStateCharacter): List<Line> {
        return listOf(Approve(), Disapprove())
    }

    override fun mySetAction(action: Action) {
        this.action = action
    }

    override fun myGetAction(): Action? {
        return this.action
    }

    override fun AIResponseFunction(brain: ConversationBrain, speaker: ShortStateCharacter, game: Game): Line {
        val action = action!!

        val implicitDeal = FinishedDeal(mapOf(speaker.player to hashSetOf(action)))

        if(brain.shortCharacter.player.brain.dealValueToMe(implicitDeal) > 0){
            return Approve()
        } else {
            return Disapprove()
        }
    }
}