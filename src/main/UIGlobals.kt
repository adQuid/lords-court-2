package main

import ui.MainUI

object UIGlobals {

    fun GUI(): MainUI {
        return Controller.singleton!!.GUI!!
    }

}