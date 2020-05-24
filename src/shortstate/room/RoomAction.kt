package shortstate.room

import main.UIGlobals
import shortstate.ShortStateGame
import shortstate.ShortStateCharacter
import shortstate.ShortStateController
import ui.specialdisplayables.Message

abstract class RoomAction {

    abstract fun clickOn(game: ShortStateGame, player: ShortStateCharacter)

    abstract fun cost(): Int

    fun canAfford(player: ShortStateCharacter): Boolean{
        return player.energy >= cost()
    }

    fun doActionIfCanAfford(game: ShortStateGame, player: ShortStateCharacter){
        if(canAfford(player)){
            player.energy -= cost()
            doAction(game, player)
        } else {
            UIGlobals.focusOn(Message("You don't have energy to do this"))
        }
    }

    abstract fun doAction(game: ShortStateGame, player: ShortStateCharacter)

    abstract fun defocusAfter(): Boolean

}