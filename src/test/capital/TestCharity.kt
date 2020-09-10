package test.capital

import aibrain.GameCase
import game.Game
import game.GameCharacter
import gamelogic.government.Capital
import gamelogic.government.GovernmentLogicModule
import gamelogic.government.actionTypes.SetTaxRate
import gamelogic.government.laws.Charity
import gamelogic.playerresources.PlayerResourceTypes
import gamelogic.territory.Territory
import gamelogic.territory.TerritoryLogicModule
import gamelogic.territory.TerritoryMap
import org.junit.Test

class TestCharity {

    private fun basicCount(game: Game, ter: Int): GameCharacter {
        val capitals = game.moduleOfType(GovernmentLogicModule.type)!! as GovernmentLogicModule

        val location = game.locations().first()

        val retval = GameCharacter("player "+game.nextID, "this should never come up in a test", true, location, game)
        game.applyTitleToCharacter(capitals.capitalById(ter).generateCountTitle(), retval)
        return retval
    }

    private fun capitalTestGame(): Game {
        val territories = TerritoryLogicModule(TerritoryMap("test"))
        territories.weekOfYear = 9
        territories.map.territories.add(Territory(territories.map.nextId, "Placeburg",0,0))
        territories.weekOfYear = 7

        val capitals = GovernmentLogicModule(listOf(Capital(territories.territories().first())), listOf())

        capitals.capitals.first().enactLaw(Charity(true))

        val game = Game(listOf(territories, capitals))
        return game
    }

    @Test
    fun test_charity_with_fish(){
        assert(test_charity(PlayerResourceTypes.FISH_NAME) >= 100)
    }

    @Test
    fun test_charity_with_insufficient_fish(){
        assert(test_charity(Territory.POPULATION_NAME) < 100)
    }

    fun test_charity(type: String): Int{
        val game = capitalTestGame()

        val ter = TerritoryLogicModule.getTerritoryLogicModule(game).map.territories.first()
        ter.resources.set(Territory.POPULATION_NAME, 100)
        ter.resources.set(Territory.FLOUR_NAME, 100) //enough to last one turn

        val capitals = game.moduleOfType(GovernmentLogicModule.type)!! as GovernmentLogicModule
        val capital = capitals.capitals.first()
        capital.resources.add(type, 1000)

        for(i in 1..10){
            game.endTurn()
        }

       return capital.territory!!.resources.resources[Territory.POPULATION_NAME]!!
    }

}