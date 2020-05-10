package test.agriculture

import game.Game
import game.Location
import game.GameCharacter
import game.gamelogicmodules.territory.Territory
import game.gamelogicmodules.territory.TerritoryLogicModule
import game.gamelogicmodules.territory.TerritoryMap
import org.junit.Test

class TestSaveAndClone {

    fun soloTestGame(): Game {
        val game = Game(listOf(TerritoryLogicModule(TerritoryMap("test", listOf(Territory("Placeburg",0,0))))))
        val defaultLocation = Location(game)

        game.locations.add(defaultLocation)

        game.addPlayer(GameCharacter("Testman", "this should never come up in a test", true, defaultLocation, game))
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