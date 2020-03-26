package main

import game.GameCharacter
import game.action.Action
import ui.Displayable
import ui.MainUI

object UIGlobals {

    fun GUI(): MainUI {
        return Controller.singleton!!.GUI!!
    }

    fun resetFocus(){
        GUI().resetFocus()
    }

    fun refresh(){
        Controller.singleton!!.GUI!!.display()
    }

    fun defocus(){
        GUI().defocus()
    }

    fun focusOn(focus: Displayable?){
        GUI().focusOn(focus)
    }

    fun appendActionsForPlayer(player: GameCharacter, actions: List<Action>){
        Controller.singleton!!.commitActionsForPlayer(player, actions)
    }

}