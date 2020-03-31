package shortstate.room.action

import game.Writ
import main.Controller
import main.UIGlobals
import shortstate.ShortStateCharacter
import shortstate.ShortStateController
import shortstate.room.RoomAction

class EnactWrit: RoomAction {

    val writ: Writ

    constructor(writ: Writ){
        this.writ = writ
    }

    override fun doAction(shortGameController: ShortStateController, player: ShortStateCharacter) {
        if(!writ.complete()){
            throw Exception("Trying to enact an imcomplete writ!")
        }
        writ.deal.actions.keys.forEach {
            UIGlobals.appendActionsForPlayer(it, writ.deal.actions[it]!!.toList())
        }
        player.player.writs.remove(writ)
    }

    override fun defocusAfter(): Boolean {
        return true
    }

    override fun toString(): String {
        return "Enact: (${writ})"
    }
}