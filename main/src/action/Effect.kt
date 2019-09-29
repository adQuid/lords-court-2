package action

import game.Game

abstract class Effect {
    abstract var probability: Double

    abstract override fun equals(other: Any?): Boolean

    abstract fun apply(game: Game)

    fun layerProbability(probability: Double): Effect{
        this.probability *= probability
        return this
    }
}