package test.units

import game.Game
import game.GameCharacter
import gamelogic.territory.TerritoryLogicModule
import gamelogic.territory.mapobjects.*
import main.Controller
import org.junit.Test
import test.capital.soloGovernmentTestGame

class TestSaveAndClone {

    fun testGameWithStructure(): Game {
        val retval = soloGovernmentTestGame()
        val territories = (retval.moduleOfType(TerritoryLogicModule.type) as TerritoryLogicModule).territories()

        StructureType.loadStructureTypes() //risky since this depends on real files
        territories.first().structures.add(Structure(retval.players.first().id, StructureType.allTypes.first()))
        return retval
    }

    fun testGameWithShip(): Game {
        val retval = soloGovernmentTestGame()
        val territories = (retval.moduleOfType(TerritoryLogicModule.type) as TerritoryLogicModule).territories()

        ShipType.loadShipTypes() //risky since this depends on real files
        territories.first().fleets.add(Fleet(retval.players.first().id, listOf(Ship(ShipType.allTypes.first()))))
        return retval
    }

    @Test
    fun testSaveAndLoadInGameWithStructures(){
        val game = testGameWithStructure()
        val game2 = Game(game.saveString())
        assert(game == game2)
    }

    @Test
    fun testSaveAndLoadInGameWithShips(){
        val game = testGameWithShip()
        val game2 = Game(game.saveString())
        assert(game == game2)
    }

}