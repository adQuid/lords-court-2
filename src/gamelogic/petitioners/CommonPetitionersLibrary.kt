package gamelogic.petitioners

import aibrain.Deal
import aibrain.FinishedDeal
import game.Game
import game.GameCharacter
import game.Writ
import gamelogic.government.Capital
import gamelogic.government.GovernmentLogicModule
import gamelogic.playerresources.GiveResource
import gamelogic.resources.ResourceTypes

object CommonPetitionersLibrary {

    val commonerNames = listOf("Peter Rook", "Daryen Miller", "Sulem Miller", "Emita Cooper")

    fun testPetitioner(capital: Capital, game: Game): GameCharacter{
        val retval = GameCharacter("Frip", "assets/portraits/faceman.png", true, capital.location, game)
        retval.petitions.add(Petition("Hello, noble lord!", null))

        return retval
    }

    fun charity(capital: Capital, game: Game): GameCharacter{
        val petitionerName = commonerNames.random()
        val governmentLogic = game.moduleOfType(GovernmentLogicModule.type) as GovernmentLogicModule
        val retval = GameCharacter(petitionerName, "assets/portraits/faceman.png", true, capital.location, game)
        retval.petitions.add(Petition("My lord, we are but a poor village, and we require gold with which to buy tools and rebuild our homes.",
            Writ("Charity to ${petitionerName}",
                giveWrit(ResourceTypes.GOLD_NAME, 1, governmentLogic.countOfCaptial(capital.terId)!!, retval), listOf(retval))))

        return retval
    }

    private fun giveWrit(key: String, amount: Int, from: GameCharacter, to: GameCharacter): Deal {
        return FinishedDeal(mapOf(from to setOf(GiveResource(to.id, key, amount))))
    }

}