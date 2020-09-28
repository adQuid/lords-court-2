package test.agriculture

import game.Game
import gamelogic.territory.Territory
import gamelogic.territory.TerritoryLogicModule
import gamelogic.territory.TerritoryMap
import org.junit.Test
import test.fixtures.territoryTestGame

class TestSaveAndClone {

    @Test
    fun test_clone(){
        val game = territoryTestGame()
        val game2 = Game(game)

        assert(game == game2)
        game2.endTurn() //just in case this makes something crash
    }

    @Test
    fun test_save(){
        val game = territoryTestGame()
        val game2 = Game(game.saveString())

        assert(game == game2)
        game2.endTurn() //just in case this makes something crash
    }

}