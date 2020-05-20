package main

import game.Game
import game.GameCharacter
import game.action.Action
import shortstate.ShortStateCharacter
import shortstate.ShortStateGame
import ui.Displayable
import ui.PerspectiveDisplayable
import ui.MainUI

object UIGlobals {

    fun activeGame(): Game {
        return Controller.singleton!!.game!!
    }

    fun activeShortGame(): ShortStateGame {
        return Controller.singleton!!.shortThreadForPlayer(playingAs()).shortGame
    }

    private fun guiOrNull(): MainUI? {
        return Controller.singleton!!.GUI
    }

    fun GUI(): MainUI {
        return Controller.singleton!!.GUI!!
    }

    fun playingAs(): ShortStateCharacter{
        return GUI().playingAs()!!
    }

    fun resetFocus(){
        if(guiOrNull() != null){
            GUI().resetFocus()
        }
    }

    fun refresh(){
        if(guiOrNull() != null){
            Controller.singleton!!.GUI!!.display()
        }
    }

    fun defocus(){
        if(guiOrNull() != null){
            GUI().defocus()
        }
    }

    fun focusOn(focus: Displayable?){
        if(guiOrNull() != null){
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