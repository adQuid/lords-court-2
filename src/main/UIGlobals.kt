package main

import game.Game
import game.GameCharacter
import game.action.Action
import shortstate.ShortStateCharacter
import ui.Displayable
import ui.MainUI

object UIGlobals {

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