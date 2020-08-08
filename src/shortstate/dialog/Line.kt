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

    abstract fun tooltipName(): String

    abstract fun tooltipDescription(): String

    abstract fun symbolicForm(context: ShortStateGame, speaker: ShortStateCharacter, target: ShortStateCharacter): List<LineBlock>

    abstract fun fullTextForm(context: ShortStateGame, speaker: ShortStateCharacter, target: ShortStateCharacter): String

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

    abstract fun possibleReplies(perspective: ShortStateCharacter): List<Line>

    open fun specialEffect(room: Room, shortGame: ShortStateGame, speaker: ShortStateCharacter){}

    abstract fun AIResponseFunction(brain: ConversationBrain, speaker: ShortStateCharacter, game: Game): Line
 }