package shortstate.room.action

import main.UIGlobals
import shortstate.ShortStateGame
import shortstate.ShortStateCharacter
import shortstate.room.RoomAction
import game.gamelogicmodules.capital.specialdisplayables.EstatesView

class ViewEstates: RoomAction() {

    override fun clickOn(game: ShortStateGame, player: ShortStateCharacter) {
        doAction(game, player)
    }

    override fun doAction(game: ShortStateGame, player: ShortStateCharacter) {
        UIGlobals.focusOn(EstatesView(player))
    }

    override fun defocusAfter(): Boolean {
        return false
    }

    override fun toString(): String {
        return "View Estates"
    }
}