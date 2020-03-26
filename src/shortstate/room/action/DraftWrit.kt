package shortstate.room.action

import aibrain.UnfinishedDeal
import game.Writ
import main.Controller
import main.UIGlobals
import shortstate.ShortStateCharacter
import shortstate.ShortStateController
import shortstate.room.RoomAction
import ui.WritConstructor

class DraftWrit: RoomAction {

    constructor(){

    }

    override fun doAction(shortGameController: ShortStateController, player: ShortStateCharacter) {
        UIGlobals.focusOn(WritConstructor(UnfinishedDeal(listOf(player.player))))
    }

    override fun toString(): String {
        return "Draft new Writ"
    }
}