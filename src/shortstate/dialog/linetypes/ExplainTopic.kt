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

class ExplainTopic: Line {

    override val type: String
        get() = GlobalLineTypeFactory.EXPLAIN_TOPIC_NAME

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
        return "Explain Topic"
    }

    override fun symbolicForm(context: ShortStateGame, speaker: ShortStateCharacter, target: ShortStateCharacter): List<LineBlock> {
        return listOf(LineBlock("Explain:"), LineBlock( "Topic: "+topic))
    }

    override fun fullTextForm(context: ShortStateGame, speaker: ShortStateCharacter, target: ShortStateCharacter): String {
        return speaker.player.infoOnTopic(topic!!)!!
    }

    override fun tooltipDescription(): String {
        return "Explain what you know about ${topic}."
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
        return true
    }

    override fun possibleReplies(perspective: ShortStateCharacter, other: ShortStateCharacter): List<Line> {
        val otherTopicsMentioned = other.player.topics().filter { isMentioned(other.player, it.name) }.map{it.name}

        return otherTopicsMentioned.map { AskAboutTopic(it) }
    }

    override fun AIResponseFunction(brain: ConversationBrain, speaker: ShortStateCharacter, game: Game): Line {
        return brain.startConversation(speaker, game)
    }

    private fun isMentioned(player: GameCharacter, topic: String): Boolean{
        val text = player.infoOnTopic(this.topic!!)!!.toLowerCase()

        return topic != this.topic && text.contains(topic.toLowerCase())
    }
}