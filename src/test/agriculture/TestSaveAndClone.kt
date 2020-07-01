package test.agriculture

import game.Game
import game.Location
import game.GameCharacter
import gamelogicmodules.territory.Territory
import gamelogicmodules.territory.TerritoryLogicModule
import gamelogicmodules.territory.TerritoryMap
import org.junit.Test

class TestSaveAndClone {

    fun soloTestGame(): Game {
        val territories = TerritoryLogicModule(TerritoryMap("test"))
        territories.map.territories.add(Territory(territories.map.nextId,"Placeburg",0,0))
        val game = Game(listOf(territories))
        val defaultLocation = Location(game, 0, 0)

        game.locations.add(defaultLocation)

        game.addPlayer(GameCharacter("Testman", "this should never come up in a test", true, defaultLocation, game))
        return game
    }

    @Test
    fun test_clone(){
        val game = soloTestGame()
        val game2 = Game(game)

        assert(game == game2)
        game2.endTurn() //just in case this makes something crash
    }

    @Test
    fun test_save(){
        val game = soloTestGame()
        val game2 = Game(soloTestGame().saveString())

        assert(game == game2)
        game2.endTurn() //just in case this makes something crash
    }

}