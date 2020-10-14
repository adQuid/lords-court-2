package test.economics

import gamelogic.economics.Construction
import gamelogic.economics.EconomicsLogicModule
import gamelogic.government.actionTypes.LaunchConstruction
import gamelogic.resources.ResourceTypes
import gamelogic.resources.Resources
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
        assert(launchConstructionTestGivenBudget(Resources()).isNotEmpty())
    }

    @Test
    fun testLaunchingConstructionPlayerCantAfford(){
        assert(launchConstructionTestGivenBudget(Resources(mapOf(ResourceTypes.GOLD_NAME to 99999))).isEmpty())
    }

    fun launchConstructionTestGivenBudget(budget: Resources): Collection<Construction>{
        setupStructureTypes()

        val testGame = soloEconomicsTestGame()
        val construction = Construction(Structure(0, StructureType.typeByName("building that needs 10 labor")))
        construction.budget.addAll(budget)

        val territories = testGame.moduleOfType(TerritoryLogicModule.type) as TerritoryLogicModule
        val territory = territories.territories().first()

        testGame.appendActionsForPlayer(testGame.players.first(), listOf(LaunchConstruction(construction, territory.id)))

        testGame.endTurn()

        return territory.constructions
    }

}