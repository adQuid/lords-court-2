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
        if(topic == null){
            return "Ask About Topic"
        } else {
            return "Ask About ${topic}"
        }
    }

    override fun symbolicForm(context: ShortStateGame, speaker: ShortStateCharacter, target: ShortStateCharacter): List<LineBlock> {
        return listOf(LineBlock("ASK ABOUT:"), LineBlock(if(topic == null) "SELECT TOPIC" else "Topic: "+topic,
            null, {perspective -> UIGlobals.focusOn(
                SelectionModal( "Ask About...",
                    target.player.topics().groupBy { it.group }.map { Tab(it.key, it.value) },
                    { topic ->
                        this.topic = topic.name; UIGlobals.defocus()
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

    override fun possibleReplies(perspective: ShortStateCharacter, other: ShortStateCharacter, game: Game): List<Line> {
        if(perspective.player.infoOnTopic(topic!!) != null){
            return listOf(ExplainTopic(topic!!))
        }
        return listOf(SimpleLine("Sorry, I don't know about that."))
    }

    override fun AIResponseFunction(brain: ConversationBrain, speaker: ShortStateCharacter, game: Game): Line {
        if(brain.shortCharacter.player.infoOnTopic(topic!!) != null){
            return ExplainTopic(topic)
        } else {
            return SimpleLine("I don't know anything about that.")
        }
    }
}