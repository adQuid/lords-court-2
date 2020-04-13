package test.controller

import main.Controller

class TestWithController(val func: (() -> Unit)) {

    fun doit(){
        Controller.singleton = Controller()
        func()
        Controller.singleton = null
    }

}