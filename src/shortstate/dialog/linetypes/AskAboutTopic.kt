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

class AskAboutTopic: Line {

    override val type: String
        get() = GlobalLineTypeFactory.ASK_TOPIC_NAME

    var topic: String? = null

    constructor(topic: String?){
        this.topic = topic
    }

    constructor(saveString: Map<String, Any?>, game: Game){
        if(saveString["TOPIC"] != null){
            topic = saveString["TOPIC"] as String
        }
    }

    override fun tooltipName(): String {
        return "Ask About Topic"
    }

    override fun symbolicForm(context: ShortStateGame, speaker: ShortStateCharacter, target: ShortStateCharacter): List<LineBlock> {
        return listOf(LineBlock("ASK ABOUT:"), LineBlock(if(topic == null) "SELECT TOPIC" else "Topic: "+topic,
            null, {perspective -> UIGlobals.focusOn(
                SelectionModal( "Ask About...",
                    listOf(
                        Tab(
                            "Topics",
                            target.player.topics().keys.toList()
                        )
                    ),
                    { topic ->
                       this.topic = topic; UIGlobals.defocus()
                    })
            )}))
    }

    override fun fullTextForm(context: ShortStateGame, speaker: ShortStateCharacter, target: ShortStateCharacter): String {
        return "Can you tell me more about ${topic}?"
    }

    override fun tooltipDescription(): String {
        return "Request mot information on something the character knows about."
    }

    override fun specialSaveString(): Map<String, Any> {
        return hashMapOf(
            "TOPIC" to topic!!
        )
    }

    override fun validToSend(): Boolean {
        return topic != null
    }

    override fun canChangeTopic(): Boolean {
        return false
    }

    override fun possibleReplies(perspective: ShortStateCharacter): List<Line> {
        return listOf()
    }

    override fun AIResponseFunction(brain: ConversationBrain, speaker: ShortStateCharacter, game: Game): Line {
        if(brain.shortCharacter.player.topics().keys.contains(topic!!)){
            return ExplainTopic(topic)
        } else {
            return SimpleLine("I don't know anything about that.")
        }
    }
}