package gamelogic.government

import aibrain.UnfinishedDeal
import gamelogic.government.actionTypes.EnactLaw
import main.UIGlobals
import shortstate.ShortStateCharacter
import ui.Displayable
import ui.commoncomponents.PrettyPrintable
import ui.specialdisplayables.contructorobjects.WritConstructor

abstract class Law: PrettyPrintable {

    companion object{
        fun makeWritFromAction(perspective: ShortStateCharacter, law: Law, capital: Capital){
            val newAction = EnactLaw(law, capital.terId)
            val newDeal = UnfinishedDeal(mapOf(perspective.player to setOf(newAction)))

            UIGlobals.focusOn(WritConstructor(newDeal))
            UIGlobals.focusOn(newAction)
        }
    }

    val type: String

    constructor(type: String){
        this.type = type
    }

    abstract fun apply(capital: Capital)

    abstract fun specialSaveString(): Map<String, Any>

    fun saveString(): Map<String, Any>{
        return specialSaveString().plus(mapOf("type" to type))
    }

    abstract fun constructorComponentFactory(capital: Capital): Displayable

}