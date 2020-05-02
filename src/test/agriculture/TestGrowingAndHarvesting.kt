package test.agriculture

import game.Game
import game.Location
import game.GameCharacter
import game.gamelogicmodules.territory.Territory
import game.gamelogicmodules.territory.TerritoryLogicModule
import org.junit.Test

class TestGrowingAndHarvesting {

    fun agroTestGame(): Game {
        val territory = Territory("Test Territory")
        territory.resources[territory.SEEDS_NAME] = 200
        territory.resources[territory.ARABLE_LAND_NAME] = 100
        territory.resources[territory.POPULATION_NAME] = 100
        val game = Game(listOf(TerritoryLogicModule(listOf(territory))))
        TerritoryLogicModule.getTerritoryLogicModule(game).weekOfYear = 7
        return game
    }

    @Test
    fun test_clone(){
        val game = agroTestGame()

        game.endTurn()
        assert(TerritoryLogicModule.getTerritoryLogicModule(game).territories.first().crops.size == 1)
        assert(TerritoryLogicModule.getTerritoryLogicModule(game).territories.first().crops[0].quantity == 100)

        game.endTurn()
        assert(TerritoryLogicModule.getTerritoryLogicModule(game).territories.first().crops.size == 2)
        assert(TerritoryLogicModule.getTerritoryLogicModule(game).territories.first().crops[1].quantity == 100)
    }


}