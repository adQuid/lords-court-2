package test.controller

import main.Controller
import org.junit.Test
import shortstate.ShortStateController
import test.fixtures.soloTestShortgame

class TestShortStateController {

    @Test
    fun testControllerRuns(){
        TestWithController {
            val testGame = soloTestShortgame()
            Controller.singleton!!.game = testGame.game
            val testController = ShortStateController(testGame)
            testController.run() //This will hang if the AI doesn't find a way to finish the round
            assert(true)
        }.doit()
    }

}