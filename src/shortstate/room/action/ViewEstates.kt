package shortstate.room.action

import main.Controller
import main.UIGlobals
import shortstate.ShortStateGame
import shortstate.ShortStateCharacter
import shortstate.ShortStateController
import shortstate.room.RoomAction
import ui.specialdisplayables.EstatesView

class ViewEstates: RoomAction() {

    override fun clickOn(game: ShortStateGame, player: ShortStateCharacter) {
        doAction(game, player)
    }

    override fun doAction(game: ShortStateGame, player: ShortStateCharacter) {
        UIGlobals.focusOn(EstatesView())
    }

    override fun defocusAfter(): Boolean {
        return false
    }

    override fun toString(): String {
        return "View Estates"
    }
}