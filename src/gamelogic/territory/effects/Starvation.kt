package gamelogic.territory.effects

import game.Effect
import game.Game

class Starvation(override var probability: Double) : Effect() {

    constructor(map: Map<String, Any>, game: Game) : this(map["prob"] as Double) {

    }

    companion object{
        val typeName = "starve"
    }

    override fun equals(other: Any?): Boolean {
        return false
    }

    override fun apply(game: Game) {
        //just a tag for now. Not certain this is a good idea
    }

    override fun description(): String {
        return "the people will starve"
    }

    override fun saveString(): Map<String, Any> {
        return mapOf("prob" to 1.0)
    }
}