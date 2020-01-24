package shortstate.dialog.linetypes

import aibrain.ConversationBrain
import shortstate.Conversation
import shortstate.dialog.Line
import shortstate.dialog.LineBlock
import game.GameCharacter
import game.Game
import shortstate.dialog.GlobalLineTypeFactory

class QuestionSuggestion: Line {

    override val type: String
        get() = GlobalLineTypeFactory.QUESTION_SUGGESTION_TYPE_NAME
    var line: SuggestAction? = null

    constructor(line: SuggestAction?){
        this.line = line
    }

    constructor(saveString: Map<String, Any>, game: Game){
        line = SuggestAction(saveString["line"] as Map<String, Any>, game)
    }

    override fun tooltipName(): String {
        return "why?"
    }

    override fun symbolicForm(): List<LineBlock> {
        var retval = mutableListOf(LineBlock("QUESTION:"))
        if(line == null){
            retval.add(LineBlock("Line:___________"))
        } else {
            retval.addAll(line!!.symbolicForm().map{block -> block.tab()})
        }
        return retval
    }

    override fun fullTextForm(speaker: GameCharacter, target: GameCharacter): String {
        return "Why would you say that?"
    }

    override fun specialSaveString(): Map<String, Any> {
        return hashMapOf(
            GlobalLineTypeFactory.TYPE_NAME to "QuestionSuggestion",
            "line" to line!!.saveString()
        )
    }

    override fun validToSend(): Boolean {
        return line != null
    }

    override fun possibleReplies(): List<Line> {
        return listOf(CiteEffect(null))
    }

    override fun specialEffect(conversation: Conversation) {
        //No special effects
    }

    override fun AIResponseFunction(brain: ConversationBrain, speaker: GameCharacter, game: Game): Line {
        return CiteEffect(brain.shortCharacter.player.brain.lastFavoriteEffects)
    }
}