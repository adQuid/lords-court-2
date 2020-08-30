package shortstate.dialog

import aibrain.ConversationBrain
import shortstate.Conversation
import game.GameCharacter
import game.Game
import shortstate.linetriggers.LineTrigger
import shortstate.ShortStateCharacter
import shortstate.ShortStateGame
import shortstate.room.Room

abstract class Line {

    var source: LineTrigger? = null
    abstract val type: String

    var specialText: String? = null

    fun withSpecialText(text: String): Line{
        specialText = text
        return this
    }

    abstract fun tooltipName(): String

    abstract fun tooltipDescription(): String

    abstract fun symbolicForm(context: ShortStateGame, speaker: ShortStateCharacter, target: ShortStateCharacter): List<LineBlock>

    abstract fun fullTextForm(context: ShortStateGame, speaker: ShortStateCharacter, target: ShortStateCharacter): String

    fun displayText(context: ShortStateGame, speaker: ShortStateCharacter, target: ShortStateCharacter): String{
        if(specialText != null){
            return specialText!!
        } else {
            return DialogFormatter
                .applyPunctuationAndCapitalization(DialogFormatter
                    .applyPronouns(fullTextForm(context, speaker, target), speaker, target))
        }
    }

    abstract fun specialSaveString(): Map<String, Any>

    fun saveString(): Map<String, Any>{
        val retval = specialSaveString().toMutableMap()

        retval.putAll(
            mapOf<String, Any>(
                GlobalLineTypeFactory.TYPE_NAME to type
            )
        )

        return retval

    }

    abstract fun validToSend(): Boolean

    abstract fun canChangeTopic(): Boolean

    abstract fun possibleReplies(perspective: ShortStateCharacter, other: ShortStateCharacter, game: Game): List<Line>

    abstract fun AIResponseFunction(brain: ConversationBrain, speaker: ShortStateCharacter, game: Game): Line

    open fun specialEffect(room: Room, shortGame: ShortStateGame, speaker: ShortStateCharacter){}
 }