package shortstate.room

import shortstate.ShortStateGame
import shortstate.ShortStatePlayer

abstract class RoomAction {

    abstract fun doAction(game: ShortStateGame, player: ShortStatePlayer)

}