package aibrain.conversationadvocates

import game.Game
import game.GameCharacter
import shortstate.ShortStateCharacter
import shortstate.dialog.Line
import shortstate.dialog.linetypes.Farewell
import shortstate.dialog.linetypes.SimpleLine

class SpecialLineAdvocate: ConversationAdvocate {

    val perspective: ShortStateCharacter

    constructor(perspective: ShortStateCharacter) : super(perspective) {
        this.perspective = perspective
    }

    override fun weight(game: Game, target: ShortStateCharacter): ConversationWeight {
        val line = bestLine(game, target)
        if(line != null){
            return ConversationWeight(15.0, line)
        }
        return ConversationWeight(0.0, SimpleLine("This should never come up"))
    }

    override fun line(game: Game, target: ShortStateCharacter): Line {
        return bestLine(game, target)!!
    }

    private fun bestLine(game: Game, target: ShortStateCharacter): Line? {
        return me.player.specialLines.filter { it.shouldGenerateLine(game.imageFor(perspective.player), null, perspective, target)}.firstOrNull()?.generateLine(game, null, me, target)
    }
}