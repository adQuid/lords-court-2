package game

abstract class Effect {

    abstract var probability: Double

    abstract override fun equals(other: Any?): Boolean

    abstract fun apply(game: Game)

    fun layerProbability(probability: Double): Effect {
        this.probability *= probability
        return this
    }

    abstract fun describe(): String

    abstract fun saveString(): Map<String, Any>
}