package game

import ui.Describable

abstract class Effect: Describable {

    abstract var probability: Double

    abstract override fun equals(other: Any?): Boolean

    abstract fun apply(game: Game)

    fun layerProbability(probability: Double): Effect {
        this.probability *= probability
        return this
    }

    abstract fun saveString(): Map<String, Any>
}