package shortstate

import dialog.Line
import game.Game

class Conversation {

    val initiator: ShortStatePlayer
    val target: ShortStatePlayer

    var lastSpeaker: ShortStatePlayer
    var lastLine: Line? = null

    constructor(initiator: ShortStatePlayer, target: ShortStatePlayer){
        this.initiator = initiator
        this.target = target
        this.lastSpeaker = target //the inititator still hasn't said anything at this point
    }

    fun otherParticipant(player: ShortStatePlayer): ShortStatePlayer{
        if (player == initiator){
            return target
        }
        if (player == target){
            return initiator
        }
        throw Exception("provided player is not in this conversation!")
    }

    fun participants(): List<ShortStatePlayer>{
        return listOf(initiator, target)
    }

    override fun equals(other: Any?): Boolean {
        if(other is Conversation){
           return this.initiator == other.initiator && this.target == other.target
        }
        return false
    }

    fun submitLine(line: Line, game: Game){
        if(line.validToSend()){
            lastLine = line
            lastSpeaker = otherParticipant(lastSpeaker)
            if(otherParticipant(lastSpeaker).player.npc){
                submitLine(otherParticipant(lastSpeaker).player.shortBrain.reactToLine(lastLine!!, lastSpeaker.player, game), game)
            }
        }
    }
}