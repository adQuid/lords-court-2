package gamelogic.government

import game.Game
import gamelogic.government.laws.Charity

abstract class Law {

    val type: String

    constructor(type: String){
        this.type = type
    }

    abstract fun apply(capital: Capital)

    abstract fun specialSaveString(): Map<String, Any>

    fun saveString(): Map<String, Any>{
        return specialSaveString().plus(mapOf("type" to type))
    }
}