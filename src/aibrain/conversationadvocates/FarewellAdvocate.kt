package aibrain.conversationadvocates

import game.Game
import game.GameCharacter
import shortstate.ShortStateCharacter
import shortstate.dialog.Line
import shortstate.dialog.linetypes.Farewell

class FarewellAdvocate: ConversationAdvocate {

    constructor(perspective: ShortStateCharacter) : super(perspective) {
    }

    override fun weight(game: Game, target: GameCharacter): ConversationWeight {
        return ConversationWeight(0.5, line(target))
    }

    override fun line(target: GameCharacter): Line {
        return Farewell()
    }
}