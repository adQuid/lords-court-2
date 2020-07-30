package test.agriculture

import game.Game
import gamelogic.territory.Territory
import gamelogic.territory.TerritoryLogicModule
import gamelogic.territory.TerritoryMap
import org.junit.Test

class TestGrowingAndHarvesting {

    private fun agroTestGame(): Game {
        val territories = TerritoryLogicModule(TerritoryMap("test"))
        territories.weekOfYear = 9
        territories.map.territories.add(Territory(territories.map.nextId, "Placeburg",0,0))
        val game = Game(listOf(territories))
        TerritoryLogicModule.getTerritoryLogicModule(game).weekOfYear = 7
        return game
    }

    @Test
    fun test_grow_and_harvest(){
        val game = agroTestGame()

        val ter = TerritoryLogicModule.getTerritoryLogicModule(game).map.territories.first()
        ter.resources.set(Territory.SEEDS_NAME, 200)
        ter.resources.set(Territory.ARABLE_LAND_NAME, 100)
        ter.resources.set(Territory.POPULATION_NAME, 100)
        //give them enough food to not have to worry about dieing
        ter.resources.set(Territory.FLOUR_NAME, 10000)
        ter.resources.set(Territory.BREAD_NAME, 100)

        game.endTurn()
        assert(ter.crops.size == 1)
        assert(ter.crops[0].quantity == 100)

        game.endTurn()
        assert(ter.crops.size == 1)

        for(i in 1..15){
            game.endTurn()
        }
        assert(ter.resources.get(Territory.SEEDS_NAME) > 0)
    }

    @Test
    fun test_milling_flour(){
        val game = agroTestGame()

        val ter = TerritoryLogicModule.getTerritoryLogicModule(game).map.territories.first()
        ter.resources.set(Territory.ARABLE_LAND_NAME, 0)
        ter.resources.set(Territory.SEEDS_NAME, 200)
        ter.resources.set(Territory.POPULATION_NAME, 100)
        //give them enough food to not have to worry about dieing
        ter.resources.set(Territory.BREAD_NAME, 100)

        game.endTurn()

        assert(ter.resources.get(Territory.FLOUR_NAME) > 0)
    }

    @Test
    fun test_population_stability(){
        val game = agroTestGame()

        val ter = TerritoryLogicModule.getTerritoryLogicModule(game).map.territories.first()
        ter.resources.set(Territory.ARABLE_LAND_NAME, 200) //200 land should be enough to support 100 population
        ter.resources.set(Territory.SEEDS_NAME, 200)
        ter.resources.set(Territory.POPULATION_NAME, 100)
        ter.resources.set(Territory.FLOUR_NAME, 1000)

        for(i in 1..100){
            game.endTurn()
            println("${ter.resources.get(Territory.SEEDS_NAME)}, ${ter.resources.get(Territory.FLOUR_NAME)}")
            assert(ter.resources.get(Territory.FLOUR_NAME) > 0)
            assert(ter.resources.get(Territory.POPULATION_NAME) > 0)
        }
    }

    @Test
    fun test_starvation(){
        val game = agroTestGame()

        val ter = TerritoryLogicModule.getTerritoryLogicModule(game).map.territories.first()
        ter.resources.set(Territory.SEEDS_NAME, 0)
        ter.resources.set(Territory.POPULATION_NAME, 100)
        ter.resources.set(Territory.FLOUR_NAME, 0)
        ter.resources.set(Territory.BREAD_NAME, 0)

        game.endTurn()
        assert(ter.resources.get(Territory.POPULATION_NAME) < 100)
        for(i in 1..9){
            game.endTurn()
        }
        assert(ter.resources.get(Territory.POPULATION_NAME) < 50)
    }

}