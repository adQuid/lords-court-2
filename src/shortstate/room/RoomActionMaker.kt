package shortstate.room

import shortstate.ShortStateCharacter
import shortstate.ShortStateGame
import ui.commoncomponents.PrettyPrintable

abstract class RoomActionMaker: PrettyPrintable {

    abstract fun onClick(game: ShortStateGame, perspective: ShortStateCharacter)

}