package game

import gamelogicmodules.cookieworld.CookieWorld
import gamelogicmodules.capital.Capital
import gamelogicmodules.capital.CapitalLogicModule
import gamelogicmodules.territory.TerritoryLogicModule
import gamelogicmodules.territory.TerritoryMap
import game.titlemaker.CookieWorldTitleFactory
import ui.specialdisplayables.worldgen.WorldEditorMainMenu
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
        val game = Game(listOf(territoryLogic, capitals))

        val pcCapital = capitals.capitals.first()
        val defaultLocation = Location(pcCapital.territory!!.x, pcCapital.territory!!.y)
        val PC = GameCharacter("Melkar the Magnificant", "assets/general/conversation frame.png", false, pcCapital.location, game)
        game.addPlayer(PC)
        game.applyTitleToCharacter(pcCapital.generateCountTitle(), PC)

        val advisor = GameCharacter("Frip", "assets/portraits/faceman.png", true, defaultLocation, game)
        game.addPlayer(advisor)

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
        //game.endTurn()
        return game
    }

}