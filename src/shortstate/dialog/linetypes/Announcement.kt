package shortstate.dialog.linetypes

import aibrain.ConversationBrain
import aibrain.Deal
import game.action.Action
import shortstate.Conversation
import shortstate.dialog.Line
import shortstate.dialog.LineBlock
import shortstate.dialog.linetypes.traits.HasAction
import game.Character
import game.Game
import shortstate.dialog.GlobalLineTypeFactory

class Announcement: Line, HasAction {

    override val type: String
        get() = GlobalLineTypeFactory.ANNOUNCEMENT_TYPE_NAME

    var action: Action? = null

    constructor(action: Action?){
        this.action = action
    }

    constructor(saveString: Map<String, Any?>, game: Game){
        if(saveString["ACTION"] != null){
            action = Action(game, saveString["ACTION"] as Map<String, Any>)
        }
    }

    override fun tooltipName(): String {
        return "Declare Action"
    }

    override fun symbolicForm(): List<LineBlock> {
        return listOf(LineBlock("ANNOUNCE:"), LineBlock(if(action == null) "Action:___________" else "Action: "+action.toString()))
    }

    override fun fullTextForm(speaker: Character, target: Character): String {
        return "I will "+action?.toString()+" by the end of the turn. Just wanted to let you know."
    }

    override fun specialSaveString(): Map<String, Any> {
        return hashMapOf(
            GlobalLineTypeFactory.TYPE_NAME to "Announcement",
            "ACTION" to action!!.saveString()
        )
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

    override fun specialEffect(conversation: Conversation) {
        //No special effects
    }

    override fun AIResponseFunction(brain: ConversationBrain, speaker: Character, game: Game): Line {
        val action = action

        val effectsILike = brain.shortCharacter.player.brain.lastFavoriteEffects!!
        val effectsOfAction = action!!.type.doAction(game, speaker)

        if(effectsILike.intersect(effectsOfAction).isNotEmpty()){
            return Approve()
        } else {
            return Disapprove()
        }
    }
}