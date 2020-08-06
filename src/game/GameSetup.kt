package game

import gamelogic.cookieworld.CookieWorld
import gamelogic.capital.Capital
import gamelogic.capital.CapitalLogicModule
import gamelogic.territory.TerritoryLogicModule
import gamelogic.territory.TerritoryMap
import game.titlemaker.CookieWorldTitleFactory
import gamelogic.playerresources.PlayerResourceModule
import gamelogic.playerresources.PlayerResourceTypes
import ui.specialdisplayables.worldgen.WorldEditorMainMenu
import game.linetriggers.LineTrigger
import shortstate.dialog.linetypes.SimpleLine
import game.linetriggers.*
import java.util.*

class GameSetup {

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

    fun setupAgricultureGame(): Game{
        val territories = TerritoryMap.fromMap(WorldEditorMainMenu.mapName)
        val territoryLogic = TerritoryLogicModule(territories)
        val capitals = CapitalLogicModule(territoryLogic.map.territories.map { Capital(it) })
        val game = Game(listOf(PlayerResourceModule(), territoryLogic, capitals))

        val pcCapital = capitals.capitals.first()
        val PC = GameCharacter("Melkar the Magnificant", "assets/general/conversation frame.png", false, pcCapital.location, game)
        PC.resources.set(PlayerResourceTypes.GOLD_NAME, 100)
        game.addPlayer(PC)
        game.applyTitleToCharacter(pcCapital.generateCountTitle(), PC)

        val advisor = GameCharacter("Kaireth", "assets/portraits/Kaireth.png", true, pcCapital.location, game)
        advisor.specialLines.add(adviceOnBadFishTrade)
        game.addPlayer(advisor)

        val fishmonger = GameCharacter("Laerten", "assets/portraits/Merchant.png", true, pcCapital.location, game)
        fishmonger.resources.set(PlayerResourceTypes.FISH_NAME, 100)
        fishmonger.specialLines.add(approachTestTrigger)
        game.addPlayer(fishmonger)

        val names = Stack<String>()
        names.addAll(listOf("Faceperson", "De Puce", "Countington", "Fred", "Fredmark", "Billybob", "Tim", "Starwin", "Artyom", "Elsvin", "Krolm", "Ashta"))
        capitals.capitals.forEach {
            if(it != pcCapital){
                val NPC = GameCharacter("Lord "+names.pop(), "assets/portraits/faceman.png", true,it.location,game)
                game.addPlayer(NPC)
                game.applyTitleToCharacter(it.generateCountTitle(), NPC)
            }
        }

        game.endTurn()
        return game
    }

}