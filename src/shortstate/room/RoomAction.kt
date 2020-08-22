package shortstate.room

import main.UIGlobals
import shortstate.ShortStateGame
import shortstate.ShortStateCharacter
import shortstate.ShortStateController
import ui.commoncomponents.PrettyPrintable
import ui.specialdisplayables.Message

abstract class RoomAction: PrettyPrintable {

    abstract fun clickOn(game: ShortStateGame, player: ShortStateCharacter)

    abstract fun cost(): Int

    fun doActionIfCanAfford(game: ShortStateGame, player: ShortStateCharacter){
        if(player.canAffordAction(this)){
            player.addEnergy(-cost())
            doAction(game, player)
        } else if(!player.player.npc){
            UIGlobals.specialFocusOn(Message("You don't have energy to do this"))
            UIGlobals.refresh()
        }
    }

    abstract fun doAction(game: ShortStateGame, player: ShortStateCharacter)

    abstract fun defocusAfter(): Boolean

    abstract override fun toString(): String

    override fun prettyPrint(context: ShortStateGame, perspective: ShortStateCharacter): String {
        if(cost() == 0){
            return toString()
        }else if(perspective.energy >= cost()){
            return toString() + " (Cost: ${cost()})"
        } else {
            return toString() + " (NOT ENOUGH ENERGY)"
        }
    }
}