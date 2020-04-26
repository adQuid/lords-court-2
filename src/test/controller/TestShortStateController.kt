package test.controller

import game.logicmodules.CookieWorld
import main.Controller
import org.junit.Test
import shortstate.ShortStateController
import test.fixtures.soloTestShortgame
import test.fixtures.shortGameResultingInDeliciousness

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

    @Test
    fun testBasicNPCCooperation(){
        TestWithController {
            val testGame = shortGameResultingInDeliciousness()
            Controller.singleton!!.game = testGame.game
            val testController = ShortStateController(testGame)
            testController.run()
            assert((Controller.singleton!!.game!!.moduleOfType("Cookieworld") as CookieWorld).deliciousness > 0)
        }.doit()
    }

}