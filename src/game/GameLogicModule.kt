package game

abstract class GameLogicModule {

    abstract val type: String

    abstract fun endTurn(): List<Effect>

    abstract fun saveString(): Map<String, Any>

    abstract fun value(perspective: GameCharacter): Double
}