package gamelogic.government

import ui.Displayable
import ui.commoncomponents.PrettyPrintable

abstract class Law: PrettyPrintable {

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