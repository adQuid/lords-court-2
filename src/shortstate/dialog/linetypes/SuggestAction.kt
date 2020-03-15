package shortstate.dialog.linetypes

import aibrain.ConversationBrain
import game.action.Action
import shortstate.Conversation
import shortstate.dialog.Line
import shortstate.dialog.LineBlock
import shortstate.dialog.linetypes.traits.HasAction
import game.GameCharacter
import game.Game
import main.Controller
import shortstate.ShortStateCharacter
import shortstate.dialog.GlobalLineTypeFactory
import ui.selectionmodal.SelectionModal
import ui.selectionmodal.Tab

class SuggestAction: Line, HasAction {

    override val type: String
        get() = GlobalLineTypeFactory.SUGGEST_ACTION_TYPE_NAME
    var action: Action? = null

    constructor(action: Action?){
        this.action = action
    }

    constructor(saveString: Map<String, Any>, game: Game){
        action = Action(game, saveString["action"] as Map<String, Any>)
    }

    override fun mySetAction(action: Action) {
        this.action = action
    }

    override fun myGetAction(): Action? {
        return this.action
    }

    override fun tooltipName(): String {
        return "Suggest Action"
    }

    override fun symbolicForm(speaker: ShortStateCharacter, target: ShortStateCharacter): List<LineBlock> {
        return listOf(LineBlock("REQUEST:"), LineBlock(if(action == null) "Action:___________" else "Action: "+action.toString(),
            {perspective -> Controller.singleton!!.GUI!!.focusOn(
                SelectionModal(
                    Controller.singleton!!.GUI!!,
                    listOf(
                        Tab("Basic Actions",
                            Controller.singleton!!.game!!.possibleActionsForPlayer(perspective.player))
                    ),
                    { action ->
                        mySetAction(action); Controller.singleton!!.GUI!!.defocus();
                    })
            )}))
    }

    override fun fullTextForm(speaker: ShortStateCharacter, target: ShortStateCharacter): String {
        return "I think you should "+action?.toString()+" this turn."
    }

    override fun specialSaveString(): Map<String, Any> {
        return hashMapOf(
            GlobalLineTypeFactory.TYPE_NAME to "SuggestAction",
            "action" to action!!.saveString()
        )
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

    override fun AIResponseFunction(brain: ConversationBrain, speaker: GameCharacter, game: Game): Line {
        throw NotImplementedError()
    }
}