package shortstate.dialog.linetypes

import aibrain.ConversationBrain
import aibrain.Deal
import aibrain.FinishedDeal
import shortstate.Conversation
import shortstate.dialog.Line
import shortstate.dialog.LineBlock
import game.GameCharacter
import game.Game
import game.Writ
import shortstate.ShortStateCharacter
import shortstate.ShortStateGame
import shortstate.dialog.GlobalLineTypeFactory
import shortstate.room.Room

class AcceptWrit: Line {

    override val type: String
        get() = GlobalLineTypeFactory.ACCEPT_DEAL_TYPE_NAME

    lateinit var writ: Writ

    constructor(writ: Writ){
        this.writ = writ
    }

    constructor(saveString: Map<String, Any?>, game: Game){
        if(saveString["writ"] != null){
            writ = Writ(saveString["writ"] as Map<String, Any>, game)
        }
    }

    override fun tooltipName(): String {
        return "Sign Writ"
    }

    override fun symbolicForm(context: ShortStateGame, speaker: ShortStateCharacter, target: ShortStateCharacter): List<LineBlock> {
        return listOf(LineBlock("ACCEPT"))
    }

    override fun fullTextForm(context: ShortStateGame,speaker: ShortStateCharacter, target: ShortStateCharacter): String {
        return "I'll be happy to sign that"
    }

    override fun tooltipDescription(): String {
        return "Add yourself to writ's signatories. The holder of this writ will be able to enact it once it's been signed by everyone."
    }

    override fun specialSaveString(): Map<String, Any> {
        return hashMapOf(
            "writ" to writ!!.saveString()
        )
    }

    override fun validToSend(): Boolean {
        return true
    }

    override fun canChangeTopic(): Boolean {
        return false
    }

    override fun possibleReplies(perspective: ShortStateCharacter): List<Line> {
        return listOf()
    }

    override fun specialEffect(room: Room, shortGame: ShortStateGame, speaker: ShortStateCharacter) {
        writ.signatories.add(speaker.player)
        if(!shortGame.shortGameScene!!.conversation!!.otherParticipant(speaker).player.writs.contains(writ)){
            shortGame.shortGameScene!!.conversation!!.otherParticipant(speaker).player.writs.add(writ)
        }
    }

    override fun AIResponseFunction(brain: ConversationBrain, speaker: ShortStateCharacter, game: Game): Line {
        return SimpleLine("Good to hear")
    }
}