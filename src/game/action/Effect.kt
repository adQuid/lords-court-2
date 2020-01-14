package game.action

import game.Game

abstract class Effect {
    val TYPE_NAME = "TYPE"

    val PROBABLITY_NAME = "PROBABILITY"
    abstract var probability: Double

    abstract override fun equals(other: Any?): Boolean

    abstract fun apply(game: Game)

    fun layerProbability(probability: Double): Effect{
        this.probability *= probability
        return this
    }

    abstract fun describe(): String

    abstract fun saveString(): Map<String, Any>
}