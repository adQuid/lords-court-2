package shortstate.dialog

import aibrain.ConversationBrain
import shortstate.Conversation
import game.Character
import game.Game

abstract class Line {

    abstract fun tooltipName(): String

    abstract fun symbolicForm(): List<LineBlock>

    abstract fun fullTextForm(speaker: Character, target: Character): String

    abstract fun validToSend(): Boolean

    abstract fun possibleReplies(): List<Line>

    abstract fun specialEffect(conversation: Conversation)

    abstract fun AIResponseFunction(brain: ConversationBrain, speaker: Character, game: Game): Line
 }