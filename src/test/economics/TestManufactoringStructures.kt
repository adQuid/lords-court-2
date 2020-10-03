package test.economics

import gamelogic.economics.EconomicsLogicModule
import gamelogic.economics.EconomicsLogicModule.Companion.LABOR_NAME
import gamelogic.resources.ResourceTypes
import gamelogic.territory.TerritoryLogicModule
import gamelogic.territory.mapobjects.Structure
import gamelogic.territory.mapobjects.StructureType
import org.junit.Test
import test.economics.Fixtures.setupStructureTypes
import test.economics.Fixtures.soloEconomicsTestGame

class TestManufactoringStructures {

    @Test
    fun testNoProductionBuildings(){
        setupStructureTypes()
        val testGame = soloEconomicsTestGame()
        val territory = (testGame.moduleOfType(TerritoryLogicModule.type) as TerritoryLogicModule).territories().first()
        val economics = testGame.moduleOfType(EconomicsLogicModule.type) as EconomicsLogicModule

        assert(economics.bestConversion(territory, listOf(LABOR_NAME), ResourceTypes.FLOUR_NAME) == null)
    }

    @Test
    fun testSimpleProductionBuilding(){
        setupStructureTypes()
        val testGame = soloEconomicsTestGame()
        val territory = (testGame.moduleOfType(TerritoryLogicModule.type) as TerritoryLogicModule).territories().first()
        val economics = testGame.moduleOfType(EconomicsLogicModule.type) as EconomicsLogicModule

        territory.structures.add(Structure(testGame.players.first().id, StructureType.typeByName("building that makes flour from just labor, but only once")))

        assert(economics.bestConversion(territory, listOf(LABOR_NAME), ResourceTypes.FLOUR_NAME) == territory.structures.first())
        assert(economics.bestConversion(territory, listOf(LABOR_NAME), ResourceTypes.ARABLE_LAND_NAME) == null)
    }

    @Test
    fun testExpendedProductionBuilding(){
        setupStructureTypes()
        val testGame = soloEconomicsTestGame()
        val territory = (testGame.moduleOfType(TerritoryLogicModule.type) as TerritoryLogicModule).territories().first()
        val economics = testGame.moduleOfType(EconomicsLogicModule.type) as EconomicsLogicModule

        val structure = Structure(testGame.players.first().id, StructureType.typeByName("building that makes flour from just labor, but only once"))
        territory.structures.add(structure)
        structure.manufatureTypeSelected = structure.type.manufactoring.first()
        structure.usesExpended = 1

        assert(economics.bestConversion(territory, listOf(LABOR_NAME), ResourceTypes.FLOUR_NAME) == null)
    }
}