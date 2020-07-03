package test.capital

import game.Game
import gamelogicmodules.capital.Capital
import gamelogicmodules.capital.CapitalLogicModule
import gamelogicmodules.capital.actionTypes.SetTaxRate
import gamelogicmodules.territory.Territory
import gamelogicmodules.territory.TerritoryLogicModule
import gamelogicmodules.territory.TerritoryMap
import org.junit.Test
import game.GameCharacter
import game.Location

class TestSaveAndClone {

    fun soloTestGame(): Game {
        val territories = TerritoryMap("test")
        territories.territories.add(Territory(territories.nextId++,"Placeburg",0,0))

        val game = Game(listOf(TerritoryLogicModule(territories), CapitalLogicModule(listOf(Capital(territories.territories[0])))))
        game.locations.add(Location(0,0))
        game.players.add(GameCharacter("name", "image", true, game.locations[0], game))

        game.actionsByPlayer.put(game.players[0], mutableListOf(SetTaxRate(territories.territories[0].id, 0.2)))
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
        val debug = soloTestGame().saveString()
        val game2 = Game(soloTestGame().saveString())

        assert(game == game2)
        game2.endTurn() //just in case this makes something crash
    }

}