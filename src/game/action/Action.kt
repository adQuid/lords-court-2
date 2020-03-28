package game.action

import game.Game
import game.GameCharacter
import game.Effect

abstract class Action {

    abstract fun doAction(game: Game, player: GameCharacter): List<Effect>

    abstract fun saveString(): Map<String, Any>

}