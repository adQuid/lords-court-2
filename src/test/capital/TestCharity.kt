package test.capital

import aibrain.GameCase
import game.Game
import game.GameCharacter
import gamelogic.economics.EconomicsLogicModule
import gamelogic.government.Capital
import gamelogic.government.GovernmentLogicModule
import gamelogic.government.actionTypes.EnactLaw
import gamelogic.government.actionTypes.SetTaxRate
import gamelogic.government.laws.Charity
import gamelogic.playerresources.PlayerResourceTypes
import gamelogic.resources.ResourceTypes
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

        val economics = EconomicsLogicModule()

        val capitals = GovernmentLogicModule(listOf(Capital(territories.territories().first())), listOf())

        capitals.capitals.first().enactLaw(Charity(true))

        val game = Game(listOf(territories, economics, capitals))
        return game
    }

    @Test
    fun test_charity_with_flour(){
        assert(test_charity(ResourceTypes.FLOUR_NAME) >= 100)
    }

    @Test
    fun test_charity_with_fish(){
        assert(test_charity(ResourceTypes.FISH_NAME) >= 100)
    }

    @Test
    fun test_charity_with_insufficient_fish(){
        assert(test_charity(null) < 100)
    }

    fun test_charity(type: String?): Int{
        val game = capitalTestGame()

        val ter = TerritoryLogicModule.getTerritoryLogicModule(game).map.territories.first()
        ter.resources.set(ResourceTypes.POPULATION_NAME, 100)
        ter.resources.set(ResourceTypes.FLOUR_NAME, 100) //enough to last one turn

        val capitals = game.moduleOfType(GovernmentLogicModule.type)!! as GovernmentLogicModule
        val capital = capitals.capitals.first()
        if(type != null){
            capital.resources.add(type, 1000)
        }

        for(i in 1..10){
            game.endTurn()
        }

       return capital.territory!!.resources.resources[ResourceTypes.POPULATION_NAME]!!
    }

    @Test
    fun test_shifting_charity(){
        val game = capitalTestGame()

        val capitalLogic = game.moduleOfType(GovernmentLogicModule.type) as GovernmentLogicModule

        val testPlayer = GameCharacter("test character", "this should not come up in a test", true, game.locations().first(), game)

        game.players.add(testPlayer)
        game.applyTitleToCharacter(capitalLogic.capitals.first().generateCountTitle(), testPlayer)

        val ter = TerritoryLogicModule.getTerritoryLogicModule(game).map.territories.first()
        ter.resources.set(ResourceTypes.POPULATION_NAME, 100)
        ter.resources.set(ResourceTypes.FLOUR_NAME, 100) //enough to last one turn

        val capitals = game.moduleOfType(GovernmentLogicModule.type)!! as GovernmentLogicModule
        val capital = capitals.capitals.first()
        capital.resources.add(ResourceTypes.FISH_NAME, 1000)

        game.appendActionsForPlayer(testPlayer, listOf(EnactLaw(Charity(false), capital.terId)))

        for(i in 1..10){
            game.endTurn()
        }
        //because we set the charity to not give on need, the capital should still have all resources
        assert(capital.resources.get(ResourceTypes.FISH_NAME) == 1000)
    }

}