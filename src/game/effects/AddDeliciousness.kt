package game.effects

import game.Game
import game.Effect

class AddDelicousness(override var probability: Double) : Effect() {

    constructor(saveString: Map<String, Any>, game: Game) : this(saveString[GlobalEffectFactory.PROBABLITY_NAME] as Double) {

    }

    override fun equals(other: Any?): Boolean {
        if(other is AddDelicousness){
            return true
        }
        return false
    }

    override fun apply(game: Game) {
        game.deliciousness = Math.min(
            game.deliciousness + this.probability, 1.0)
    }
    override fun toString(): String{
        return "add deliciousness with probability $probability"
    }

    override fun saveString(): Map<String, Any> {
        return hashMapOf(
            GlobalEffectFactory.TYPE_NAME to GlobalEffectFactory.ADD_DELICIOUSNESS_NAME,
            GlobalEffectFactory.PROBABLITY_NAME to probability
        )
    }

    override fun describe(): String {
        return "deliciousness levels would increase by $probability"
    }
}