package test.controller

import gamelogic.cookieworld.CookieWorld
import main.Controller
import org.junit.Test
import shortstate.ShortStateController
import test.controller.TestWithController
import test.fixtures.soloTestShortgame
import test.fixtures.shortGameResultingInDeliciousness
import test.fixtures.testGameWithTwoLocations

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
    fun testControllerRunsWithMultipleLocations(){
        TestWithController {
            val testGame = testGameWithTwoLocations()
            Controller.singleton!!.newGame(testGame)
            while(Controller.singleton!!.game!!.turn == 1){
                Thread.sleep(20)
            }
        }.doit()
    }

    @Test
    fun testBasicNPCCooperation(){
        TestWithController {
            val testGame = shortGameResultingInDeliciousness()
            Controller.singleton!!.game = testGame.game
            val testController = ShortStateController(testGame)
            testController.run()
            val cookieworld = Controller.singleton!!.game!!.moduleOfType("Cookieworld") as CookieWorld
            assert(cookieworld.deliciousness > 0)
            assert(cookieworld.hasMilk.size == 2)
        }.doit()
    }

    @Test
    fun testActionsDontCrossLocations(){
        TestWithController {
            val testGame = testGameWithTwoLocations()
            Controller.singleton!!.newGame(testGame) //This will hang if the AI doesn't find a way to finish the round
            // there's a baker and a milkman here, but they can't talk to each other so there should be no deliciousness
            assert((Controller.singleton!!.game!!.moduleOfType("Cookieworld") as CookieWorld).deliciousness == 0.0)
        }.doit()
    }

}