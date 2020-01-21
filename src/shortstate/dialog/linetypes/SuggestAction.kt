package shortstate.dialog.linetypes

import aibrain.ConversationBrain
import game.action.Action
import shortstate.Conversation
import shortstate.dialog.Line
import shortstate.dialog.LineBlock
import shortstate.dialog.linetypes.traits.HasAction
import game.Character
import game.Game
import shortstate.dialog.GlobalLineTypeFactory

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

    override fun symbolicForm(): List<LineBlock> {
        return listOf(LineBlock("REQUEST:"), LineBlock(if(action == null) "Action:___________" else "Action: "+action.toString()))
    }

    override fun fullTextForm(speaker: Character, target: Character): String {
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

    override fun AIResponseFunction(brain: ConversationBrain, speaker: Character, game: Game): Line {
        throw NotImplementedError()
    }
}