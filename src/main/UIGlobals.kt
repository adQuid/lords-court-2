package main

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

}