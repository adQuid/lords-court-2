package test.capital

import game.Game
import game.Location
import game.GameCharacter
import game.gamelogicmodules.capital.Capital
import game.gamelogicmodules.capital.CapitalLogicModule
import game.gamelogicmodules.territory.Territory
import game.gamelogicmodules.territory.TerritoryLogicModule
import org.junit.Test

class TestSaveAndClone {

    fun soloTestGame(): Game {
        val territories = listOf(Territory("Placeburg"))

        val game = Game(listOf(TerritoryLogicModule("test", territories), CapitalLogicModule(listOf(Capital(territories[0])))))
        return game
    }

    @Test
    fun test_clone(){
        val game = soloTestGame()
        val game2 = Game(game)

        assert(game == game2)
    }

    @Test
    fun test_save(){
        val game = soloTestGame()
        val game2 = Game(soloTestGame().saveString())

        assert(game == game2)
    }

}