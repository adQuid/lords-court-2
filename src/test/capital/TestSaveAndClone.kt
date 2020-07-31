package test.capital

import game.Game
import gamelogic.capital.Capital
import gamelogic.capital.CapitalLogicModule
import gamelogic.capital.actionTypes.SetTaxRate
import gamelogic.territory.Territory
import gamelogic.territory.TerritoryLogicModule
import gamelogic.territory.TerritoryMap
import org.junit.Test
import game.GameCharacter

class TestSaveAndClone {

    fun soloTestGame(): Game {
        val territories = TerritoryMap("test")
        territories.territories.add(Territory(territories.nextId++,"Placeburg",0,0))

        val game = Game(listOf(TerritoryLogicModule(territories), CapitalLogicModule(listOf(Capital(territories.territories[0])))))
        val player = GameCharacter("name", "image", true, game.locations().first(), game)
        player.resources.set("some resource", 10)
        game.players.add(player)

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
        val game2 = Game(game.saveString())

        assert(game == game2)
        assert(game.players[0].npc == game2.players[0].npc)
        assert(game.players[0].resources.resources == game2.players[0].resources.resources)
        game2.endTurn() //just in case this makes something crash
    }

}