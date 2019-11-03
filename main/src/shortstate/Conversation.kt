package shortstate

import shortstate.dialog.Line
import game.Game
import javafx.application.Platform
import main.Controller
import shortstate.dialog.Memory

class Conversation {

    val initiator: ShortStateCharacter
    val target: ShortStateCharacter

    var lastSpeaker: ShortStateCharacter
    var lastLine: Line? = null

    var age = 0

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

    fun submitLine(line: Line, game: ShortStateGame){
        if(line.validToSend()){
            age++
            lastLine = line
            otherParticipant(lastSpeaker).player.memory.add(Memory(line))
            lastSpeaker = otherParticipant(lastSpeaker)
        }
        game.doAIIfAppropriate()
        try{
            Platform.runLater { Controller.singleton!!.GUI!!.refocus() }
        } catch(exception: Exception){
            //Do nothing. This is scotch tape because sort state games might be made before UI starts
        }
    }
}