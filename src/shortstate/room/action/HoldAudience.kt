package shortstate.room.action

import main.UIGlobals
import shortstate.ShortStateCharacter
import shortstate.ShortStateGame
import shortstate.room.RoomAction

class HoldAudience: RoomAction {

    constructor(){

    }

    override fun clickOn(game: ShortStateGame, player: ShortStateCharacter) {
        doActionIfCanAfford(game, player)
    }

    override fun cost(): Int {
        return 300
    }

    override fun doAction(game: ShortStateGame, player: ShortStateCharacter) {
        UIGlobals.displayMessage("Nobody else comes forward.")
    }

    override fun defocusAfter(): Boolean {
        return false
    }

    override fun toString(): String {
        return "Await Petitioners"
    }
}