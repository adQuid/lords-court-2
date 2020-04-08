package test.controller

import javafx.application.Application
import main.Controller
import main.UIGlobals
import ui.MainUI

class TestWithUI(val func: (() -> Unit)) {

    fun doit(){
        UIGlobals.disabledForTesting = true
        Controller.singleton = Controller()
        Thread{
            Application.launch(MainUI::class.java)
        }.start()
        while(Controller.singleton!!.GUI == null){
            Thread.sleep(10)
        }
        func()
        UIGlobals.disabledForTesting = false
    }

}