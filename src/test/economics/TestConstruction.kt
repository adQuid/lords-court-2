package test.economics

import gamelogic.economics.Construction
import gamelogic.economics.EconomicsLogicModule
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
    fun testConstructionRequiringResourceConversion(){
        val totalBudget = Resources(mapOf(ResourceTypes.GOLD_NAME to 10))
        assert(launchConstructionTestGivenBudget(totalBudget, totalBudget, 2).structures.isNotEmpty())
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