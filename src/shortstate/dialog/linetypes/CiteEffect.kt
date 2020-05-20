package shortstate.dialog.linetypes

import aibrain.*
import game.Effect
import shortstate.Conversation
import shortstate.dialog.Line
import shortstate.dialog.LineBlock
import game.GameCharacter
import game.Game
import shortstate.dialog.GlobalLineTypeFactory
import game.effects.GlobalEffectFactory
import main.Controller
import main.UIGlobals
import shortstate.ShortStateCharacter
import shortstate.ShortStateGame
import shortstate.room.Room
import ui.commoncomponents.AppendableList
import ui.componentfactory.EffectChooser

class CiteEffect: Line {

    override val type: String
        get() = GlobalLineTypeFactory.CITE_EFFECT_TYPE_NAME

    val deal: FinishedDeal
    var effects: MutableList<Effect>
    val effectsDisplayList = AppendableList<Effect>()

    constructor(deal: FinishedDeal){
        this.deal = deal
        effects = mutableListOf()
    }

    constructor(deal: FinishedDeal, effects: List<Effect>){
        this.effects = effects.toMutableList()
        this.deal = deal
    }

    constructor(saveString: Map<String, Any?>, game: Game){
        this.deal = FinishedDeal(saveString["deal"] as Map<String, Any>, game)
        if(saveString["effects"] != null){
            effects =  (saveString["effects"] as List<Map<String, Any>>).map { map -> GlobalEffectFactory.fromMap(map, game) }.toMutableList()
        } else {
            effects = mutableListOf()
        }
    }

    override fun tooltipName(): String {
        return "Give Reason"
    }

    override fun symbolicForm(context: ShortStateGame,  speaker: ShortStateCharacter, target: ShortStateCharacter): List<LineBlock> {
        var retval = mutableListOf(LineBlock("CITE:"))
        if(effects.isEmpty()){
            retval.add(LineBlock("Effect:___________", { UIGlobals.focusOn(action(speaker))}))
        } else {
            retval.addAll(effects!!.map{ effect -> LineBlock("Effect: ${effect.toString()}")})
        }

        return retval
    }

    override fun fullTextForm(context: ShortStateGame, speaker: ShortStateCharacter, target: ShortStateCharacter): String {
        return "I have some very good reasons: " + effects?.map { effect -> effect.describe() }
    }

    override fun specialSaveString(): Map<String, Any> {
        return hashMapOf(
            "effects" to effects!!.map { effect -> effect.saveString() },
            "deal" to deal.saveString()
        )
    }
    
    override fun validToSend(): Boolean {
        return effects.isNotEmpty()
    }

    override fun canChangeTopic(): Boolean {
        return false
    }

    override fun possibleReplies(perspective: ShortStateCharacter): List<Line> {
        return listOf()
    }

    override fun specialEffect(room: Room, conversation: Conversation, speaker: ShortStateCharacter) {
        //No special effects
    }

    override fun AIResponseFunction(brain: ConversationBrain, speaker: GameCharacter, game: Game): Line {
        return Disapprove()
    }

    private fun action(speaker: ShortStateCharacter): EffectChooser{
        return EffectChooser(
            DealCase(deal).effectsOfDeal(listOf(GameCase(Controller.singleton!!.game!!.imageFor(speaker.player), speaker.player))),
        { effect -> effects.add(effect); UIGlobals.defocus()})
    }
}