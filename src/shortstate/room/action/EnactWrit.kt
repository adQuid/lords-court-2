package shortstate.room.action

import game.Writ
import main.Controller
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

        }
    }
}