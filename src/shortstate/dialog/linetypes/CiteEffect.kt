package shortstate.dialog.linetypes

import aibrain.*
import game.Effect
import shortstate.Conversation
import shortstate.dialog.Line
import shortstate.dialog.LineBlock
import game.GameCharacter
import game.Game
import game.effects.AddDelicousness
import shortstate.dialog.GlobalLineTypeFactory
import game.effects.GlobalEffectFactory
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import main.Controller
import shortstate.ShortStateCharacter
import ui.Displayable
import ui.commoncomponents.AppendableList
import ui.componentfactory.EffectChooser
import ui.componentfactory.UtilityComponentFactory
import ui.totalHeight
import ui.totalWidth

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

    override fun symbolicForm(speaker: ShortStateCharacter, target: ShortStateCharacter): List<LineBlock> {
        var retval = mutableListOf(LineBlock("CITE:"))
        if(effects.isEmpty()){
            retval.add(LineBlock("Effect:___________", { Controller.singleton!!.GUI!!.focusOn(action(speaker))}))
        } else {
            retval.addAll(effects!!.map{ effect -> LineBlock("Effect: ${effect.toString()}")})
        }

        return retval
    }

    override fun fullTextForm(speaker: ShortStateCharacter, target: ShortStateCharacter): String {
        return "I have some very good reasons: " + effects?.map { effect -> effect.describe() }
    }

    override fun specialSaveString(): Map<String, Any> {
        return hashMapOf(
            GlobalLineTypeFactory.TYPE_NAME to "CiteEffect",
            "effects" to effects!!.map { effect -> effect.saveString() },
            "deal" to deal.saveString()
        )
    }
    
    override fun validToSend(): Boolean {
        return effects.isNotEmpty()
    }

    override fun possibleReplies(): List<Line> {
        return listOf()
    }

    override fun specialEffect(conversation: Conversation) {
        //No special effects
    }

    override fun AIResponseFunction(brain: ConversationBrain, speaker: GameCharacter, game: Game): Line {
        return Disapprove()
    }

    private fun action(speaker: ShortStateCharacter): EffectChooser{
        return EffectChooser(
            DealCase(deal).effectsOfDeal(listOf(GameCase(Controller.singleton!!.game!!.imageFor(speaker.player), speaker.player))),
        { effect -> effects.add(effect); Controller.singleton!!.GUI!!.defocus()})
    }
}