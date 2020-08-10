package aibrain.conversationadvocates

import game.Game
import game.GameCharacter
import shortstate.ShortStateCharacter
import shortstate.dialog.Line
import shortstate.dialog.linetypes.Farewell

class FarewellAdvocate: ConversationAdvocate {

    constructor(perspective: ShortStateCharacter) : super(perspective) {
    }

    override fun weight(game: Game, target: ShortStateCharacter): ConversationWeight {
        return ConversationWeight(0.5, line(game, target))
    }

    override fun line(game: Game, target: ShortStateCharacter): Line {
        return Farewell()
    }
}