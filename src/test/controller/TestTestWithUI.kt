package test.controller

import javafx.application.Application
import main.Controller
import main.UIGlobals
import org.junit.Test
import ui.MainUI

class TestTestWithUI {

    @Test
    fun testit(){
        TestWithUI {assert(true)}.doit()
    }
}