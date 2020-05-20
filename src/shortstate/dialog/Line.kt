package shortstate.dialog

import aibrain.ConversationBrain
import shortstate.Conversation
import game.GameCharacter
import game.Game
import shortstate.ShortStateCharacter
import shortstate.ShortStateGame
import shortstate.room.Room

abstract class Line {

    abstract val type: String

    abstract fun tooltipName(): String

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

    abstract fun specialEffect(room: Room, conversation: Conversation, speaker: ShortStateCharacter)

    abstract fun AIResponseFunction(brain: ConversationBrain, speaker: GameCharacter, game: Game): Line
 }