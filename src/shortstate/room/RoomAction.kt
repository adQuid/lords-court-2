package shortstate.room

import main.UIGlobals
import shortstate.ShortStateGame
import shortstate.ShortStateCharacter
import shortstate.ShortStateController
import ui.specialdisplayables.Message

abstract class RoomAction {

    abstract fun clickOn(game: ShortStateGame, player: ShortStateCharacter)

    abstract fun cost(): Int

    fun doActionIfCanAfford(game: ShortStateGame, player: ShortStateCharacter){
        if(player.canAffordAction(this)){
            player.energy -= cost()
            doAction(game, player)
        } else if(!player.player.npc){
            UIGlobals.focusOn(Message("You don't have energy to do this"))
        }
    }

    abstract fun doAction(game: ShortStateGame, player: ShortStateCharacter)

    abstract fun defocusAfter(): Boolean

}