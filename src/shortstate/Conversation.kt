package shortstate

import aibrain.UnfinishedDeal
import game.action.Action
import main.UIGlobals
import shortstate.dialog.Line
import shortstate.dialog.LineMemory
import shortstate.dialog.GlobalLineTypeFactory
import shortstate.dialog.linetypes.*
import shortstate.room.Room
import ui.componentfactory.ConversationComponentFactory

class Conversation {

    val ROOM_NAME = "ROOM"
    val room: Room
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

    constructor(room: Room, initiator: ShortStateCharacter, target: ShortStateCharacter){
        this.room = room
        this.initiator = initiator
        this.target = target
        this.lastSpeaker = target //the inititator still hasn't said anything at this point
    }

    constructor(parent: ShortStateGame, saveString: Map<String, Any?>){
        room = Room(parent, saveString[ROOM_NAME] as Map<String, Any>)
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
            ROOM_NAME to room.saveString(),
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

    fun bestLineToLeaveConversation(perspective: ShortStateCharacter): Line{
        if(lastLine == null){
            return AbandonConversation()
        } else {
            return Farewell()
        }
    }

    fun lineOptions(perspective: ShortStateCharacter): List<Line>{
        var retval = listOf<Line>()
        if(lastLine != null
            && lastLine!!.possibleReplies(perspective, otherParticipant(perspective), UIGlobals.activeGame()).isNotEmpty()){
            retval = lastLine!!.possibleReplies(perspective, otherParticipant(perspective), UIGlobals.activeGame())
        }
        if(lastLine == null || lastLine!!.canChangeTopic() || retval.size == 0){
            retval = retval.plus(defaultConversationLines(perspective))
        }
        retval = retval.plus(perspective.player.specialLines.filter{it.shouldGenerateLine(UIGlobals.activeGame().imageFor(perspective.player), lastLine, perspective, otherParticipant(perspective))}
            .map { it.generateLine(UIGlobals.activeGame(), lastLine, perspective, otherParticipant(perspective)) })
        return retval
    }

    fun defaultConversationLines(perspective: ShortStateCharacter): List<Line>{
        return listOfNotNull(
            if(true) AskAboutTradableGoods() else null,
            if(otherParticipant(perspective).player.titles.flatMap { title -> title.reportsEntitled }.isNotEmpty() && perspective.player.titles.flatMap { title -> title.reportsEntitled }.isNotEmpty()) RequestReport(null) else null,
            if(perspective.player.actionsReguarding(otherParticipant(perspective).player).isNotEmpty()) OfferDeal(UnfinishedDeal(participants().map { it.player })) else null,
            if(perspective.player.actionsReguarding(otherParticipant(perspective).player).isNotEmpty()) RequestAdviceForDeal(UnfinishedDeal(participants().map { it.player })) else null,
            if(perspective.player.writs.filter{it.deal.actions.getOrDefault(this.otherParticipant(perspective).player, setOf()).isNotEmpty()}.isNotEmpty()) OfferWrit(null) else null,
            if(otherParticipant(perspective).player.topics().isNotEmpty()) AskAboutTopic(null) else null,
            bestLineToLeaveConversation(perspective)
        )
    }

    override fun equals(other: Any?): Boolean {
        if(other is Conversation){
           return this.room == other.room && this.initiator == other.initiator && this.target == other.target
        }
        return false
    }

    fun submitLine(line: Line, game: ShortStateGame){
        println("\"${line.displayText(game, otherParticipant(lastSpeaker), lastSpeaker)}\"(${line::class}) submitted by ${otherParticipant(lastSpeaker)}")
        if(line.validToSend()){
            line.specialEffect(room, game, otherParticipant(lastSpeaker))
            if(line.source != null){
                line.source!!.data["calls"] = (line.source!!.data["calls"] as Int) + 1
            }
            age++
            lastLine = line
            lastSpeaker = otherParticipant(lastSpeaker)
            otherParticipant(lastSpeaker).player.memory.lines.add(LineMemory(line))
        } else {
            throw Exception("Tried to send invalid line!")
        }
    }
}