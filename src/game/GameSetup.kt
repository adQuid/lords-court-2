package game

import game.gamelogicmodules.CookieWorld
import game.gamelogicmodules.capital.Capital
import game.gamelogicmodules.capital.CapitalLogicModule
import game.gamelogicmodules.territory.TerritoryLogicModule
import game.gamelogicmodules.territory.TerritoryMap
import game.titlemaker.CookieWorldTitleFactory
import java.util.*

class GameSetup {

    fun setupCookieworld(): Game{
        val game = Game(listOf(CookieWorld()))

        val defaultLocation = Location(game)

        game.locations.add(defaultLocation)

        val PC = GameCharacter("Melkar the Magnificant", "assets/general/conversation frame.png", false, defaultLocation, game)
        game.applyTitleToCharacter(CookieWorldTitleFactory.makeBakerTitle("Cookies"), PC)
        game.addPlayer(PC)

        val NPC = GameCharacter("Frip", "assets/portraits/faceman.png", true, defaultLocation, game)
        NPC.titles.add(CookieWorldTitleFactory.makeMilkmanTitle())
        game.addPlayer(NPC)

        return game
    }

    fun setupAgricultureGame(): Game{
        val territories = TerritoryMap.fromMap("maps/testland")
        val territoryLogic = TerritoryLogicModule(territories)
        val capitals = CapitalLogicModule(territoryLogic.map.territories.map { Capital(it) })
        val game = Game(listOf(territoryLogic, capitals))

        val defaultLocation = Location(game)

        game.locations.add(defaultLocation)

        val pcCapital = capitals.capitals.first()
        val PC = GameCharacter("Melkar the Magnificant", "assets/general/conversation frame.png", false, defaultLocation, game)
        game.addPlayer(PC)
        game.applyTitleToCharacter(pcCapital.generateCountTitle(), PC)

        val names = Stack<String>()
        names.addAll(listOf("Faceperson", "De Puce", "Countington", "Fred", "Fredmark", "Billybob", "Tim"))
        capitals.capitals.forEach {
            if(it != pcCapital){
                val location = Location(game)
                game.locations.add(location)
                val NPC = GameCharacter("Lord "+names.pop(), "assets/portraits/faceman.png", true,location,game)
                game.addPlayer(NPC)
                game.applyTitleToCharacter(it.generateCountTitle(), NPC)
            }
        }

        return game
    }

}