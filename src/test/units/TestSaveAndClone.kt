package test.units

import game.Game
import org.junit.Test
import test.units.Fixtures.testGameWithFishingShip
import test.units.Fixtures.testGameWithStructure

class TestSaveAndClone {

    @Test
    fun testSaveAndLoadInGameWithStructures(){
        val game = testGameWithStructure()
        val game2 = Game(game.saveString())
        assert(game == game2)
    }

    @Test
    fun testSaveAndLoadInGameWithShips(){
        val game = testGameWithFishingShip()
        val game2 = Game(game.saveString())
        assert(game == game2)
    }

}