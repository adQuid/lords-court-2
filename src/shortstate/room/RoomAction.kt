package shortstate.room

import shortstate.ShortStateGame
import shortstate.ShortStateCharacter

abstract class RoomAction {

    abstract fun doAction(game: ShortStateGame, player: ShortStateCharacter)

}