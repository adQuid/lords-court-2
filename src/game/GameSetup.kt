package game

import aibrain.FinishedDeal
import gamelogic.cookieworld.CookieWorld
import gamelogic.government.Capital
import gamelogic.government.GovernmentLogicModule
import gamelogic.territory.TerritoryLogicModule
import gamelogic.territory.TerritoryMap
import game.titlemaker.CookieWorldTitleFactory
import gamelogic.government.Kingdom
import gamelogic.government.actionTypes.GiveTerritory
import gamelogic.government.actionTypes.SetTaxRate
import gamelogic.playerresources.GiveResource
import gamelogic.playerresources.PlayerResourceModule
import gamelogic.playerresources.PlayerResourceTypes
import gamelogic.territory.Territory
import scenario.*
import ui.specialdisplayables.worldgen.WorldEditorMainMenu
import java.util.*

object GameSetup {

    fun setupCookieworld(): Game{
        val cookieWorld = CookieWorld()
        val game = Game(listOf(cookieWorld))

        val PC = GameCharacter("Melkar the Magnificant", "assets/general/conversation frame.png", false, cookieWorld.locations.first(), game)
        game.applyTitleToCharacter(CookieWorldTitleFactory.makeBakerTitle("Cookies"), PC)
        game.addPlayer(PC)

        val NPC = GameCharacter("Frip", "assets/portraits/faceman.png", true, cookieWorld.locations.first(), game)
        NPC.titles.add(CookieWorldTitleFactory.makeMilkmanTitle())
        game.addPlayer(NPC)

        return game
    }

}