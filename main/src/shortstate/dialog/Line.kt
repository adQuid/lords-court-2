package shortstate.dialog

import shortstate.Conversation
import game.Character

abstract class Line {

    abstract fun tooltipName(): String

    abstract fun symbolicForm(): List<LineBlock>

    abstract fun fullTextForm(speaker: Character, target: Character): String

    abstract fun validToSend(): Boolean

    abstract fun possibleReplies(): List<Line>

    abstract fun specialEffect(conversation: Conversation)
 }