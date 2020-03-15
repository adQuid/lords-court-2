package shortstate.dialog.linetypes

import aibrain.ConversationBrain
import game.Effect
import shortstate.Conversation
import shortstate.dialog.Line
import shortstate.dialog.LineBlock
import game.GameCharacter
import game.Game
import shortstate.dialog.GlobalLineTypeFactory
import game.effects.GlobalEffectFactory
import shortstate.ShortStateCharacter

class CiteEffect: Line {

    override val type: String
        get() = GlobalLineTypeFactory.CITE_EFFECT_TYPE_NAME

    var effects: List<Effect>? = null

    constructor(effects: List<Effect>?){
        this.effects = effects
    }

    constructor(saveString: Map<String, Any?>, game: Game){
        if(saveString["effects"] != null){
            effects =  (saveString["effects"] as List<Map<String, Any>>).map { map -> GlobalEffectFactory.fromMap(map, game) }
        }
    }

    override fun tooltipName(): String {
        return "Give Reason"
    }

    override fun symbolicForm(speaker: ShortStateCharacter, target: ShortStateCharacter): List<LineBlock> {
        var retval = mutableListOf(LineBlock("CITE:"))
        if(effects == null){
            retval.add(LineBlock("Effect:___________"))
        } else {
            retval.addAll(effects!!.map{ effect -> LineBlock("Effect: ${effect.toString()}")})
        }

        return retval
    }

    override fun fullTextForm(speaker: ShortStateCharacter, target: ShortStateCharacter): String {
        return "I have some very good reasons: " + effects!!.map { effect -> effect.describe() }
    }

    override fun specialSaveString(): Map<String, Any> {
        return hashMapOf(
            GlobalLineTypeFactory.TYPE_NAME to "CiteEffect",
            "effects" to effects!!.map { effect -> effect.saveString() }
        )
    }
    
    override fun validToSend(): Boolean {
        return effects != null
    }

    override fun possibleReplies(): List<Line> {
        return listOf()
    }

    override fun specialEffect(conversation: Conversation) {
        //No special effects
    }

    override fun AIResponseFunction(brain: ConversationBrain, speaker: GameCharacter, game: Game): Line {
       throw NotImplementedError()
    }
}