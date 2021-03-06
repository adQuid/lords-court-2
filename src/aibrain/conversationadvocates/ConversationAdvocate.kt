package aibrain.conversationadvocates

import game.Game
import game.GameCharacter
import shortstate.ShortStateCharacter
import shortstate.dialog.Line

abstract class ConversationAdvocate {

    val me: ShortStateCharacter

    constructor(perspective: ShortStateCharacter){
        me = perspective
    }

    abstract fun weight(game: Game, target: ShortStateCharacter): ConversationWeight

    abstract fun line(game: Game, target: ShortStateCharacter): Line

}