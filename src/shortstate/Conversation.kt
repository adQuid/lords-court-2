package shortstate

import shortstate.dialog.Line
import shortstate.dialog.Memory

class Conversation {

    val INITIATOR_NAME = "INITER"
    val initiator: ShortStateCharacter
    val TARGET_NAME = "TARGET"
    val target: ShortStateCharacter

    val LAST_SPEAKER_NAME = "SPEAKER"
    var lastSpeaker: ShortStateCharacter
    val LAST_LINE_NAME = "LINE"
    var lastLine: Line? = null

    var age = 0

    constructor(initiator: ShortStateCharacter, target: ShortStateCharacter){
        this.initiator = initiator
        this.target = target
        this.lastSpeaker = target //the inititator still hasn't said anything at this point
    }

    fun saveString(): Map<String, Any>{
        return hashMapOf(
            INITIATOR_NAME to initiator.player.id,
            TARGET_NAME to target.player.id,
            LAST_SPEAKER_NAME to lastSpeaker.player.id,
            LAST_LINE_NAME to {lastLine!!.saveString(); if(lastLine != null) else null}
        )
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

    fun submitLine(line: Line, game: ShortStateGame){
        println("\"${line.fullTextForm(lastSpeaker.player, otherParticipant(lastSpeaker).player)}\" submitted by ${otherParticipant(lastSpeaker)}")
        if(line.validToSend()){
            line.specialEffect(this)
            age++
            lastLine = line
            lastSpeaker = otherParticipant(lastSpeaker)
            otherParticipant(lastSpeaker).player.memory.add(Memory(line))
        }
    }
}