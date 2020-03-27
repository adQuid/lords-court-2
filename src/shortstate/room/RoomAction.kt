package shortstate.room

import shortstate.ShortStateGame
import shortstate.ShortStateCharacter
import shortstate.ShortStateController

abstract class RoomAction {

    abstract fun doAction(shortGameController: ShortStateController, player: ShortStateCharacter)

    abstract fun defocusAfter(): Boolean

}