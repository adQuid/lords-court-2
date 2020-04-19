package shortstate.room

import shortstate.ShortStateGame
import shortstate.ShortStateCharacter
import shortstate.ShortStateController

abstract class RoomAction {

    abstract fun doAction(game: ShortStateGame, player: ShortStateCharacter)

    abstract fun defocusAfter(): Boolean

}