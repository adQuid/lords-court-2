package game

import dialog.Line

class Conversation {

    val initiator: Player
    val target: Player

    var lastSpeaker: Player
    var lastLine: Line? = null

    constructor(initiator: Player, target: Player){
        this.initiator = initiator
        this.target = target
        this.lastSpeaker = target //the inititator still hasn't said anything at this point
    }

    fun otherParticipant(player: Player): Player{
        if (player == initiator){
            return target
        }
        if (player == target){
            return initiator
        }
        throw Exception("provided player is not in this conversation!")
    }

    fun participants(): List<Player>{
        return listOf(initiator, target)
    }

    override fun equals(other: Any?): Boolean {
        if(other is Conversation){
           return this.initiator == other.initiator && this.target == other.target
        }
        return false
    }

    fun submitLine(line: Line){
        lastLine = line
        lastSpeaker = otherParticipant(lastSpeaker)
        if(otherParticipant(lastSpeaker).npc){
            lastLine = otherParticipant(lastSpeaker).shortBrain.reactToLine(lastLine!!, lastSpeaker)
        }
    }
}