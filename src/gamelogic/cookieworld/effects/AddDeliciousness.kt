package gamelogic.cookieworld.effects

import game.Game
import game.Effect
import gamelogic.cookieworld.CookieWorld
import shortstate.ShortStateCharacter

class AddDelicousness(override var probability: Double) : Effect() {

    companion object{
        val typeName = "AddDel"
    }

    constructor(saveString: Map<String, Any>, game: Game) : this(saveString[CookieWorldEffectFactory.PROBABLITY_NAME] as Double) {

    }

    override fun equals(other: Any?): Boolean {
        if(other is AddDelicousness){
            return true
        }
        return false
    }

    override fun apply(game: Game) {
        val logic = CookieWorld.getCookieWorld(game)
        logic.deliciousness = Math.min(
            logic.deliciousness + this.probability, 1.0)
    }
    override fun toString(): String{
        return "add deliciousness with probability $probability"
    }

    override fun saveString(): Map<String, Any> {
        return hashMapOf(
            CookieWorldEffectFactory.TYPE_NAME to typeName,
            CookieWorldEffectFactory.PROBABLITY_NAME to probability
        )
    }

    override fun tooltip(perspective: ShortStateCharacter): String {
        return "add Deliciousness"
    }

    override fun description(): String {
        return "deliciousness levels would increase by $probability"
    }
}