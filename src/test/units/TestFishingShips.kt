package test.units

import gamelogic.resources.ResourceTypes
import org.junit.Test
import test.units.Fixtures.testGameWithEconomicsFishingShip

class TestFishingShips {

    @Test
    fun testFishingShipsGatherFish(){
        val testGame = testGameWithEconomicsFishingShip()

        testGame.endTurn()

        assert(testGame.players.first().privateResources.get(ResourceTypes.FISH_NAME) > 0)
    }

}