package test.units

import game.Game
import gamelogic.resources.ResourceTypes
import gamelogic.territory.TerritoryLogicModule
import gamelogic.territory.mapobjects.*
import test.capital.soloGovernmentTestGame
import test.economics.Fixtures.soloEconomicsTestGame

object Fixtures {

    fun testGameWithStructure(): Game {
        val retval = soloGovernmentTestGame()
        val territories = (retval.moduleOfType(TerritoryLogicModule.type) as TerritoryLogicModule).territories()

        StructureType.loadStructureTypes() //risky since this depends on real files
        territories.first().structures.add(Structure(retval.players.first().id, StructureType.allTypes.first()))
        return retval
    }

    fun testGameWithFishingShip(): Game {
        val retval = soloGovernmentTestGame()
        val territories = (retval.moduleOfType(TerritoryLogicModule.type) as TerritoryLogicModule).territories()

        ShipType.loadShipTypes() //risky since this depends on real files
        territories.first().fleets.add(Fleet(retval.players.first().id, listOf(Ship(ShipType.allTypes.first{it.resourceExtractions.getOrDefault(ResourceTypes.FISH_NAME, -1) != -1}))))
        return retval
    }

    fun testGameWithEconomicsFishingShip(): Game {
        val retval = soloEconomicsTestGame()
        val territories = (retval.moduleOfType(TerritoryLogicModule.type) as TerritoryLogicModule).territories()

        ShipType.loadShipTypes() //risky since this depends on real files
        territories.first().fleets.add(Fleet(retval.players.first().id, listOf(Ship(ShipType.allTypes.first{it.resourceExtractions.getOrDefault(ResourceTypes.FISH_NAME, -1) != -1}))))
        return retval
    }

}