package test.capital

import aibrain.GameCase
import game.Game
import game.Location
import game.GameCharacter
import gamelogicmodules.capital.Capital
import gamelogicmodules.capital.CapitalLogicModule
import gamelogicmodules.capital.Count
import gamelogicmodules.capital.actionTypes.SetTaxRate
import gamelogicmodules.territory.Territory
import gamelogicmodules.territory.TerritoryLogicModule
import gamelogicmodules.territory.TerritoryMap
import org.junit.Test

class TestTaxation {

    private fun basicCount(game: Game, ter: Int): GameCharacter {
        val capitals = game.moduleOfType(CapitalLogicModule.type)!! as CapitalLogicModule

        val location = game.locations.first()

        val retval = GameCharacter("player "+game.nextID, "this should never come up in a test", true, location, game)
        game.applyTitleToCharacter(capitals.capitalById(ter).generateCountTitle(), retval)
        return retval
    }

    private fun capitalTestGame(): Game {
        val territories = TerritoryLogicModule(TerritoryMap("test"))
        territories.weekOfYear = 9
        territories.map.territories.add(Territory(territories.map.nextId, "Placeburg",0,0))
        territories.weekOfYear = 7

        val capitals = CapitalLogicModule(listOf(Capital(territories.territories().first())))

        val game = Game(listOf(territories, capitals))
        game.locations.add(Location(game))

        return game
    }

    @Test
    fun test_setting_taxes(){
        val game = capitalTestGame()
        val capitals = game.moduleOfType(CapitalLogicModule.type)!! as CapitalLogicModule

        game.players.add(basicCount(game, capitals.capitals.first().terId))

        game.appendActionsForPlayer(game.players.first(), listOf(SetTaxRate(capitals.capitals.first().terId, 1.0)))
        game.endTurn()
        assert(capitals.capitals.first().taxes[Territory.FLOUR_NAME] == 1.0)
    }

    @Test
    fun test_laffer_curve(){
        val lowTaxResult = test_tax_rate(0.1)
        val medTaxResult = test_tax_rate(0.3)
        val highTaxResult = test_tax_rate(0.7)

        println("$lowTaxResult, $medTaxResult, $highTaxResult")
        assert(medTaxResult > lowTaxResult)
        assert(medTaxResult > highTaxResult)
    }

    fun test_tax_rate(rate: Double): Int{
        val game = capitalTestGame()

        val ter = TerritoryLogicModule.getTerritoryLogicModule(game).map.territories.first()
        ter.resources.set(Territory.SEEDS_NAME, 300)
        ter.resources.set(Territory.POPULATION_NAME, 100)
        ter.resources.set(Territory.FLOUR_NAME, 1000)
        ter.resources.set(Territory.BREAD_NAME, 100)
        ter.resources.set(Territory.ARABLE_LAND_NAME, 300)

        val capitals = game.moduleOfType(CapitalLogicModule.type)!! as CapitalLogicModule
        val capital = capitals.capitals.first()
        capital.taxes[Territory.FLOUR_NAME] = rate

        for(i in 1..GameCase.LOOKAHEAD){
            game.endTurn()
        }

       return capital.resources.resources[Territory.FLOUR_NAME]!!
    }

}