package test.capital

import aibrain.ForecastBrain
import gamelogic.government.GovernmentLogicModule
import gamelogic.playerresources.PlayerResourceTypes
import gamelogic.territory.Territory
import main.Controller
import org.junit.Test
import shortstate.ShortStateController
import shortstate.ShortStateGame
import test.controller.TestWithController
import test.fixtures.*

class TestAI {

    @Test
    fun testSetTaxes(){
        TestWithController {
            val testGame = soloGovernmentTestGame()
            Controller.singleton!!.game = testGame
            val testController = ShortStateController(ShortStateGame(testGame, testGame.locations().first()))
            testController.run() //This will hang if the AI doesn't find a way to finish the round

            val govModule = testGame.moduleOfType(GovernmentLogicModule.type) as GovernmentLogicModule

            assert(govModule.capitals.first().taxes.get(Territory.FLOUR_NAME)!! > 0.1)
        }.doit()
    }

    @Test
    fun testMultipleCountTitles(){
        TestWithController {
            val testGame = twoCapitalTestGame()
            Controller.singleton!!.game = testGame
            val testController = ShortStateController(ShortStateGame(testGame, testGame.locations().first()))
            testController.run() //This will hang if the AI doesn't find a way to finish the round

            val govModule = testGame.moduleOfType(GovernmentLogicModule.type) as GovernmentLogicModule

            govModule.capitals.forEach {
                assert(it.taxes.get(Territory.FLOUR_NAME)!! > 0.1)
            }
        }.doit()
    }

}