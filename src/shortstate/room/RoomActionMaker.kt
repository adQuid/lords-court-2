package shortstate.room

import shortstate.ShortStateCharacter
import shortstate.ShortStateGame

abstract class RoomActionMaker {

    abstract fun onClick(game: ShortStateGame, perspective: ShortStateCharacter)

    abstract override fun toString(): String

}