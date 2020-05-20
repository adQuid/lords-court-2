package shortstate.dialog.linetypes

import aibrain.ConversationBrain
import shortstate.Conversation
import shortstate.dialog.Line
import shortstate.dialog.LineBlock
import game.GameCharacter
import game.Game
import shortstate.ShortStateCharacter
import shortstate.ShortStateGame
import shortstate.dialog.GlobalLineTypeFactory
import shortstate.room.Room

class QuestionOffer: Line {

    override val type: String
        get() = GlobalLineTypeFactory.QUESTION_OFFER_TYPE_NAME
    var line: OfferDeal? = null

    constructor(line: OfferDeal?){
        this.line = line
    }

    constructor(saveString: Map<String, Any>, game: Game){
        line = OfferDeal(saveString["line"] as Map<String, Any>, game)
    }

    override fun tooltipName(): String {
        return "Why?"
    }

    override fun symbolicForm(context: ShortStateGame, speaker: ShortStateCharacter, target: ShortStateCharacter): List<LineBlock> {
        var retval = mutableListOf(LineBlock("QUESTION:"))
        if(line == null){
            retval.add(LineBlock("Line:___________"))
        } else {
            retval.addAll(line!!.symbolicForm(context, speaker, target).map{block -> block.tab()})
        }
        return retval
    }

    override fun fullTextForm(context: ShortStateGame, speaker: ShortStateCharacter, target: ShortStateCharacter): String {
        return "Why would you say that?"
    }

    override fun specialSaveString(): Map<String, Any> {
        return hashMapOf(
            "line" to line!!.saveString()
        )
    }

    override fun validToSend(): Boolean {
        return line != null
    }

    override fun canChangeTopic(): Boolean {
        return false
    }

    override fun possibleReplies(perspective: ShortStateCharacter): List<Line> {
        return listOf(CiteEffect(line!!.deal.toFinishedDeal()))
    }

    override fun specialEffect(room: Room, conversation: Conversation, speaker: ShortStateCharacter) {
        //No special effects
    }

    override fun AIResponseFunction(brain: ConversationBrain, speaker: GameCharacter, game: Game): Line {
        return CiteEffect(line!!.deal.toFinishedDeal(), brain.shortCharacter.player.brain.justifyDeal(line!!.deal, brain.shortCharacter.player))
    }
}