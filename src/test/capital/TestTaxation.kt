package test.capital

import aibrain.GameCase
import game.Game
import game.GameCharacter
import gamelogic.economics.EconomicsLogicModule
import gamelogic.government.Capital
import gamelogic.government.GovernmentLogicModule
import gamelogic.government.actionTypes.SetTaxRate
import gamelogic.resources.ResourceTypes
import gamelogic.territory.Territory
import gamelogic.territory.TerritoryLogicModule
import gamelogic.territory.TerritoryMap
import org.junit.Test

class TestTaxation {

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

        val game = Game(listOf(territories, economics, capitals))
        return game
    }

    @Test
    fun test_setting_taxes(){
        val game = capitalTestGame()
        val capitals = game.moduleOfType(GovernmentLogicModule.type)!! as GovernmentLogicModule

        game.players.add(basicCount(game, capitals.capitals.first().terId))

        game.appendActionsForPlayer(game.players.first(), listOf(SetTaxRate(capitals.capitals.first().terId, 1.0)))
        game.endTurn()
        assert(capitals.capitals.first().taxes[ResourceTypes.FLOUR_NAME] == 1.0)
    }

    @Test
    fun test_laffer_curve(){
        val lowTaxResult = test_tax_rate(0.1)
        val medTaxResult = test_tax_rate(0.3)
        val highTaxResult = test_tax_rate(0.7)
        val maxTaxResult = test_tax_rate(1.0)

        println("$lowTaxResult, $medTaxResult, $highTaxResult")
        assert(medTaxResult > lowTaxResult)
        assert(medTaxResult > highTaxResult)
        assert(highTaxResult > maxTaxResult)
    }

    fun test_tax_rate(rate: Double): Int{
        val game = capitalTestGame()

        val ter = TerritoryLogicModule.getTerritoryLogicModule(game).map.territories.first()
        ter.resources.set(ResourceTypes.SEEDS_NAME, 300)
        ter.resources.set(ResourceTypes.POPULATION_NAME, 100)
        ter.resources.set(ResourceTypes.FLOUR_NAME, 1000)
        ter.resources.set(ResourceTypes.BREAD_NAME, 100)
        ter.resources.set(ResourceTypes.ARABLE_LAND_NAME, 300)

        val capitals = game.moduleOfType(GovernmentLogicModule.type)!! as GovernmentLogicModule
        val capital = capitals.capitals.first()
        capital.taxes[ResourceTypes.FLOUR_NAME] = rate

        for(i in 1..GameCase.LOOKAHEAD){
            game.endTurn()
        }

       return capital.resources.resources[ResourceTypes.FLOUR_NAME]!!
    }

}