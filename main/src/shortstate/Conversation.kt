package shortstate

import shortstate.dialog.Line
import game.Game

class Conversation {

    val initiator: ShortStateCharacter
    val target: ShortStateCharacter

    var lastSpeaker: ShortStateCharacter
    var lastLine: Line? = null

    constructor(initiator: ShortStateCharacter, target: ShortStateCharacter){
        this.initiator = initiator
        this.target = target
        this.lastSpeaker = target //the inititator still hasn't said anything at this point
    }

    fun otherParticipant(player: ShortStateCharacter): ShortStateCharacter{
        if (player == initiator){
            return target
        }
        if (player == target){
            return initiator
        }
        throw Exception("provided player is not in this conversation!")
    }

    fun participants(): List<ShortStateCharacter>{
        return listOf(initiator, target)
    }

    override fun equals(other: Any?): Boolean {
        if(other is Conversation){
           return this.initiator == other.initiator && this.target == other.target
        }
        return false
    }

    fun doAIIfAppropriate(game: Game){
        if(otherParticipant(lastSpeaker).player.npc){
            submitLine(otherParticipant(lastSpeaker).convoBrain.reactToLine(lastLine, lastSpeaker.player, game), game)
        }
    }

    fun submitLine(line: Line, game: Game){
        if(line.validToSend()){
            lastLine = line
            lastSpeaker = otherParticipant(lastSpeaker)
            doAIIfAppropriate(game)
        }
    }
}