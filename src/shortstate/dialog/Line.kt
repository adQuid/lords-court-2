package shortstate.dialog

import aibrain.ConversationBrain
import shortstate.Conversation
import game.GameCharacter
import game.Game
import javafx.scene.Scene
import shortstate.ShortStateCharacter
import ui.Displayable

abstract class Line {

    abstract val type: String

    abstract fun tooltipName(): String

    abstract fun symbolicForm(speaker: ShortStateCharacter, target: ShortStateCharacter): List<LineBlock>

    abstract fun fullTextForm(speaker: ShortStateCharacter, target: ShortStateCharacter): String

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

    abstract fun specialEffect(conversation: Conversation, speaker: ShortStateCharacter)

    abstract fun AIResponseFunction(brain: ConversationBrain, speaker: GameCharacter, game: Game): Line
 }