package gamelogic.petitioners

import aibrain.Deal
import aibrain.FinishedDeal
import game.Game
import game.GameCharacter
import game.Writ
import gamelogic.economics.Construction
import gamelogic.economics.EconomicsLogicModule
import gamelogic.government.Capital
import gamelogic.government.GovernmentLogicModule
import gamelogic.government.actionTypes.LaunchConstruction
import gamelogic.playerresources.GiveResource
import gamelogic.resources.ResourceTypes
import gamelogic.resources.Resources
import gamelogic.territory.mapobjects.Structure
import gamelogic.territory.mapobjects.StructureType

object CommonPetitionersLibrary {

    val commonerNames = listOf("Peter Rook", "Daryen Miller", "Sulem Miller", "Ayren Cooper")

    fun testPetitioner(capital: Capital, game: Game): GameCharacter{
        val retval = GameCharacter("Frip", "assets/portraits/faceman.png", true, capital.location, game)
        retval.petitions.add(Petition("Hello, noble lord!", null))

        return retval
    }

    fun charity(capital: Capital, game: Game): GameCharacter{
        val petitionerName = commonerNames.random()
        val governmentLogic = game.moduleOfType(GovernmentLogicModule.type) as GovernmentLogicModule
        val retval = GameCharacter(petitionerName, "assets/portraits/Commoner_1.png", true, capital.location, game)
        retval.petitions.add(Petition("My lord, we are but a poor village, and we require gold with which to buy tools and rebuild our homes.",
            Writ("Charity to ${petitionerName}",
                giveWrit(ResourceTypes.GOLD_NAME, 1, governmentLogic.countOfCaptial(capital.terId)!!, retval), listOf(retval))))

        return retval
    }

    fun buildMill(capital: Capital, game: Game): GameCharacter{
        val petitionerName = commonerNames.random()
        val governmentLogic = game.moduleOfType(GovernmentLogicModule.type) as GovernmentLogicModule

        val retval = GameCharacter(petitionerName, "assets/portraits/Commoner_1.png", true, capital.location, game)

        val petition = Petition("There's a creek down below Ned's farm, and I think it would be the perfect place to build a mill. Just think of all the time we could save if we used the river to grind our grain.",
            Writ("Construct new mill",
                buildDeal(StructureType.typeByName("Water Mill"), 9, governmentLogic.countOfCaptial(capital.terId)!!, capital.terId), listOf()))
        retval.petitions.add(petition)

        return retval
    }

    private fun giveWrit(key: String, amount: Int, from: GameCharacter, to: GameCharacter): Deal {
        return FinishedDeal(mapOf(from to setOf(GiveResource(to.id, key, amount))))
    }

    private fun buildDeal(structureType: StructureType, gold: Int, count: GameCharacter, territory: Int): Deal{
        val resources = Resources()

        resources.add(ResourceTypes.GOLD_NAME, gold)

        return FinishedDeal(mapOf(
            count to setOf(LaunchConstruction(Construction(Structure(count.id, structureType), resources), territory))))
    }
}