package test.economics

import aibrain.FinishedDeal
import aibrain.ForecastBrain
import game.GameCharacter
import game.action.Action
import gamelogic.economics.Construction
import gamelogic.economics.EconomicsLogicModule
import gamelogic.government.GovernmentLogicModule
import gamelogic.government.actionTypes.LaunchConstruction
import gamelogic.resources.ResourceTypes
import gamelogic.resources.Resources
import gamelogic.territory.Territory
import gamelogic.territory.TerritoryLogicModule
import gamelogic.territory.mapobjects.Structure
import gamelogic.territory.mapobjects.StructureType
import org.junit.Test
import test.economics.Fixtures.gameWithConstruction
import test.economics.Fixtures.setupStructureTypes
import test.economics.Fixtures.soloEconomicsTestGame
import test.fixtures.soloTestGame
import test.fixtures.territoryTestGame

class TestConstruction {

    @Test
    fun testBasicConstruction(){
        setupStructureTypes()

        val construction = Construction(Structure(0, StructureType.typeByName("building that needs 10 labor")))
        construction.budget.add(EconomicsLogicModule.LABOR_NAME, 5)
        val testGame = gameWithConstruction(construction)

        testGame.endTurn()

        assert(construction.spentResources.get(EconomicsLogicModule.LABOR_NAME) == 5)
        construction.budget.add(EconomicsLogicModule.LABOR_NAME, 5)

        val territories = testGame.moduleOfType(TerritoryLogicModule.type) as TerritoryLogicModule
        val territory = territories.territories().first()

        assert(territory.structures.isEmpty())

        testGame.endTurn()

        assert(territory.structures.isNotEmpty())
    }

    @Test
    fun testLaunchConstruction(){
        assert(launchConstructionTestGivenBudget(Resources(), Resources()).constructions.isNotEmpty())
    }

    @Test
    fun testLaunchingConstructionPlayerCantAfford(){
        assert(launchConstructionTestGivenBudget(Resources(mapOf(ResourceTypes.GOLD_NAME to 99999)), Resources()).constructions.isEmpty())
    }

    @Test
    fun testConstructionStartingWithLabor(){
        val result = launchConstructionTestGivenBudget(Resources(mapOf(EconomicsLogicModule.LABOR_NAME to 10)), Resources(mapOf(EconomicsLogicModule.LABOR_NAME to 10)), 2)
        assert(result.constructions.isEmpty())
        assert(result.structures.isNotEmpty())
    }

    @Test
    fun testConstructionRequiringResourceConversion(){
        val totalBudget = Resources(mapOf(ResourceTypes.GOLD_NAME to 10))
        assert(launchConstructionTestGivenBudget(totalBudget, totalBudget, 2).structures.isNotEmpty())
    }

    @Test
    fun testConstructionRequiringResourceConversionWithOverbudget(){
        val totalBudget = Resources(mapOf(ResourceTypes.GOLD_NAME to 20))
        assert(launchConstructionTestGivenBudget(totalBudget, totalBudget, 2).structures.isNotEmpty())
    }

    @Test
    fun testThinkingAboutConstructionThatCompletes(){
        setupStructureTypes()

        val budget = Resources(mapOf(ResourceTypes.GOLD_NAME to 10))

        val game = soloEconomicsTestGame()

        val govModule = game.moduleOfType(GovernmentLogicModule.type) as GovernmentLogicModule
        val capital = govModule.capitals.first()

        val character = game.players[0]
        game.addResourceForCharacter(character, budget)
        val brain = ForecastBrain(character) //should I be doing this? The Character class already makes a brain for itself...

        brain.thinkAboutNextTurn(game)

        val construction = Construction(Structure(0, StructureType.typeByName("building that needs 10 labor")))
        construction.budget.addAll(budget)

        val constructAction = LaunchConstruction(construction, capital.terId)
        val map = mutableMapOf<GameCharacter, Set<Action>>()
        map[character] = setOf(constructAction)

        brain.dealScoreToCharacter(FinishedDeal(map), character)
    }

    fun launchConstructionTestGivenBudget(budget: Resources, playerResources: Resources, turns: Int = 1): Territory{
        setupStructureTypes()

        val testGame = soloEconomicsTestGame()
        val construction = Construction(Structure(0, StructureType.typeByName("building that needs 10 labor")))
        construction.budget.addAll(budget)

        val territories = testGame.moduleOfType(TerritoryLogicModule.type) as TerritoryLogicModule
        val territory = territories.territories().first()

        testGame.addResourceForCharacter(testGame.players.first(), playerResources)
        testGame.appendActionsForPlayer(testGame.players.first(), listOf(LaunchConstruction(construction, territory.id)))

        for(i in 1..turns){
            testGame.endTurn()
        }

        return territory
    }

}