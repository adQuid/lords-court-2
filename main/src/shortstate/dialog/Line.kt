package shortstate.dialog

import shortstate.Conversation

abstract class Line {

    abstract fun tooltipName(): String

    abstract fun symbolicForm(): List<LineBlock>

    abstract fun fullTextForm(): String

    abstract fun validToSend(): Boolean

    abstract fun possibleReplies(): List<Line>

    abstract fun specialEffect(conversation: Conversation)
 }