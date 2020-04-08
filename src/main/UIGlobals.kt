package main

import game.Game
import game.GameCharacter
import game.action.Action
import shortstate.ShortStateCharacter
import ui.Displayable
import ui.MainUI

object UIGlobals {

    var disabledForTesting = false

    fun activeGame(): Game {
        return Controller.singleton!!.game!!
    }

    fun GUI(): MainUI {
        return Controller.singleton!!.GUI!!
    }

    fun playingAs(): ShortStateCharacter{
        return GUI().playingAs()
    }

    fun resetFocus(){
        if(!disabledForTesting){
            GUI().resetFocus()
        }
    }

    fun refresh(){
        if(!disabledForTesting){
            Controller.singleton!!.GUI!!.display()
        }
    }

    fun defocus(){
        if(!disabledForTesting){
            GUI().defocus()
        }
    }

    fun focusOn(focus: Displayable?){
        if(!disabledForTesting){
            GUI().focusOn(focus)
        }
    }

    fun appendActionsForPlayer(player: GameCharacter, actions: List<Action>){
        Controller.singleton!!.commitActionsForPlayer(player, actions)
    }

    fun totalHeight(): Double{
        return GUI().totalHeight
    }

    fun totalWidth(): Double{
        return GUI().totalWidth
    }
}