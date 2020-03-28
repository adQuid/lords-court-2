package shortstate

import aibrain.UnfinishedDeal
import javafx.scene.Scene
import shortstate.dialog.Line
import shortstate.dialog.Memory
import shortstate.dialog.GlobalLineTypeFactory
import shortstate.dialog.linetypes.Announcement
import shortstate.dialog.linetypes.OfferDeal
import shortstate.dialog.linetypes.OfferWrit
import shortstate.dialog.linetypes.RequestReport
import ui.componentfactory.ConversationComponentFactory

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

    //TODO: Loosen coupling
    val display = ConversationComponentFactory(this)

    constructor(initiator: ShortStateCharacter, target: ShortStateCharacter){
        this.initiator = initiator
        this.target = target
        this.lastSpeaker = target //the inititator still hasn't said anything at this point
    }

    constructor(parent: ShortStateGame, saveString: Map<String, Any?>){
        initiator = parent.shortPlayerForLongPlayer(parent.game.characterById(saveString[INITIATOR_NAME] as Int)!!)!!
        target = parent.shortPlayerForLongPlayer(parent.game.characterById(saveString[TARGET_NAME] as Int)!!)!!
        lastSpeaker = parent.shortPlayerForLongPlayer(parent.game.characterById(saveString[LAST_SPEAKER_NAME] as Int)!!)!!
        if(saveString[LAST_LINE_NAME] == null){
            lastLine = null
        } else {
            lastLine = GlobalLineTypeFactory.fromMap(saveString[LAST_LINE_NAME] as Map<String, Any>, parent.game)
        }
    }

    fun saveString(): Map<String, Any?>{
        return hashMapOf(
            INITIATOR_NAME to initiator.player.id,
            TARGET_NAME to target.player.id,
            LAST_SPEAKER_NAME to lastSpeaker.player.id,
            LAST_LINE_NAME to {if(lastLine != null){lastLine!!.saveString()} else{ null}}()
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

    fun defaultConversationLines(perspective: ShortStateCharacter): List<Line>{
        return listOf(
            Announcement(null),
            RequestReport(null),
            OfferDeal(UnfinishedDeal(participants().map { it.player })),
            OfferWrit(null)
        )
    }

    override fun equals(other: Any?): Boolean {
        if(other is Conversation){
           return this.initiator == other.initiator && this.target == other.target
        }
        return false
    }

    fun submitLine(line: Line, game: ShortStateGame){
        println("\"${line.fullTextForm(lastSpeaker, otherParticipant(lastSpeaker))}\"(${line::class}) submitted by ${otherParticipant(lastSpeaker)}")
        if(line.validToSend()){
            line.specialEffect(this)
            age++
            lastLine = line
            lastSpeaker = otherParticipant(lastSpeaker)
            otherParticipant(lastSpeaker).player.memory.add(Memory(line))
        }
    }
}