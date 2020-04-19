package shortstate.room.action

import aibrain.UnfinishedDeal
import main.UIGlobals
import shortstate.ShortStateCharacter
import shortstate.ShortStateController
import shortstate.ShortStateGame
import shortstate.room.RoomAction
import ui.contructorobjects.WritConstructor

class DraftWrit: RoomAction {

    constructor(){

    }

    override fun doAction(game: ShortStateGame, player: ShortStateCharacter) {
        UIGlobals.focusOn(WritConstructor(UnfinishedDeal(listOf(player.player))))
    }

    override fun defocusAfter(): Boolean {
        return false
    }

    override fun toString(): String {
        return "Draft new Writ"
    }
}