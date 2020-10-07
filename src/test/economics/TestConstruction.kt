package test.economics

import gamelogic.economics.Construction
import gamelogic.economics.EconomicsLogicModule
import gamelogic.territory.TerritoryLogicModule
import gamelogic.territory.mapobjects.Structure
import gamelogic.territory.mapobjects.StructureType
import org.junit.Test
import test.economics.Fixtures.gameWithConstruction
import test.economics.Fixtures.setupStructureTypes

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

}