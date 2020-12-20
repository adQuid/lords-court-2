package test.petitioners

import game.Game
import game.GameCharacter
import gamelogic.government.Capital
import gamelogic.government.GovernmentLogicModule
import gamelogic.government.Kingdom
import gamelogic.government.laws.Charity
import gamelogic.petitioners.PetitionersLogicModule
import gamelogic.territory.Territory
import gamelogic.territory.TerritoryLogicModule
import gamelogic.territory.TerritoryMap
import gamelogic.territory.mapobjects.StructureType
import org.junit.Test

class TestSaveAndClone {

    fun petitionerTestGame(): Game{
        val territories = TerritoryMap("test")
        territories.territories.add(Territory(territories.nextId++,"Placeburg",0,0))

        val govLogic = GovernmentLogicModule(listOf(Capital(territories.territories[0])),
            listOf(Kingdom("Test Kingdom", territories.territories)))

        govLogic.capitals.first().enactLaw(Charity(false))

        val game = Game(listOf(
            TerritoryLogicModule(territories),
            govLogic,
            PetitionersLogicModule()
        ))
        val player = GameCharacter("name", "image", true, game.locations().first(), game)
        game.applyTitleToCharacter(govLogic.capitals.first().generateCountTitle(), player)
        game.applyTitleToCharacter(govLogic.kingdoms.first().generateKingTitle(), player)
        game.players.add(player)

        return game
    }

    @Test
    fun test_clone(){
        val game = petitionerTestGame()
        val game2 = Game(game)

        assert(game == game2)
        game2.endTurn() //just in case this makes something crash
    }

    @Test
    fun test_save(){
        StructureType.loadStructureTypes()

        val game = petitionerTestGame()
        val game2 = Game(game.saveString())

        assert(game == game2)
        game2.endTurn() //just in case this makes something crash
    }

}