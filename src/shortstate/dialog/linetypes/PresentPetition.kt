package shortstate.dialog.linetypes

import aibrain.ConversationBrain
import aibrain.Deal
import aibrain.FinishedDeal
import aibrain.UnfinishedDeal
import shortstate.Conversation
import shortstate.dialog.Line
import shortstate.dialog.LineBlock
import game.GameCharacter
import game.Game
import game.action.Action
import gamelogic.petitioners.Petition
import main.Controller
import main.UIGlobals
import shortstate.ShortStateCharacter
import shortstate.ShortStateGame
import shortstate.dialog.GlobalLineTypeFactory
import shortstate.room.Room

class PresentPetition: Line {

    override val type: String
        get() = GlobalLineTypeFactory.PRESENT_PETITION_NAME
    var petition: Petition

    constructor(petition: Petition){
        this.petition = petition
    }

    constructor(saveString: Map<String, Any>, game: Game){
        petition = Petition(saveString["petition"] as Map<String, Any>, game)
    }

    override fun tooltipName(): String {
        return "Present Petition"
    }

    override fun symbolicForm(context: ShortStateGame, speaker: ShortStateCharacter, target: ShortStateCharacter): List<LineBlock> {
        val dealBlock = if(petition==null){
            return listOf(LineBlock("Petition:___________"))
        } else {
            return listOf(LineBlock("[CLICK FOR DETAILS]", { UIGlobals.focusOn(petition.writ)}))
        }
    }

    override fun fullTextForm(context: ShortStateGame, speaker: ShortStateCharacter, target: ShortStateCharacter): String {
        if(petition != null){
            return petition.text
        } else {
            return "I present to you this petition..."
        }
    }

    override fun tooltipDescription(): String {
        return "FILLER"
    }

    override fun specialSaveString(): Map<String, Any> {
        return hashMapOf(
            "deal" to petition.saveString()
        )
    }

    override fun validToSend(): Boolean {
        return petition != null
    }

    override fun canChangeTopic(): Boolean {
        return true
    }

    override fun possibleReplies(perspective: ShortStateCharacter, other: ShortStateCharacter, game: Game): List<Line> {
        if(petition.writ != null){
            return listOf(AcceptWrit(petition.writ!!), RejectDeal(petition.writ!!.deal))
        } else {
            return listOf(SimpleLine("Thanks for telling me."))
        }
    }

    override fun specialEffect(room: Room, shortGame: ShortStateGame, speaker: ShortStateCharacter) {
        if(petition.writ != null){
            shortGame.shortGameScene!!
                .conversation!!.otherParticipant(speaker).player.writs.add(petition.writ!!)
        }
        speaker.player.petitions.remove(petition)
    }

    override fun AIResponseFunction(brain: ConversationBrain, speaker: ShortStateCharacter, game: Game): Line {
        return SimpleLine("My pitiful AI brain is not yet equipped to review petitions.")
    }
}