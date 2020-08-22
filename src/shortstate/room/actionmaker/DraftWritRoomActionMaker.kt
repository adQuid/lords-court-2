package shortstate.room.actionmaker

import aibrain.UnfinishedDeal
import main.UIGlobals
import shortstate.ShortStateCharacter
import shortstate.ShortStateGame
import shortstate.room.RoomActionMaker
import ui.specialdisplayables.contructorobjects.WritConstructor

class DraftWritRoomActionMaker: RoomActionMaker {

    constructor() {

    }

    override fun onClick(game: ShortStateGame, perspective: ShortStateCharacter) {
        UIGlobals.focusOn(WritConstructor(UnfinishedDeal(listOf(UIGlobals.playingAs().player))))
    }

    override fun prettyPrint(context: ShortStateGame, perspective: ShortStateCharacter): String {
        return "Draft new Writ"
    }
}