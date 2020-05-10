package game

import game.gamelogicmodules.CookieWorld
import game.gamelogicmodules.capital.Capital
import game.gamelogicmodules.capital.CapitalLogicModule
import game.gamelogicmodules.territory.Territory
import game.gamelogicmodules.territory.TerritoryLogicModule
import game.titlemaker.CookieWorldTitleFactory

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
        val territories = TerritoryLogicModule("test", listOf(Territory("Placeburg",0,0)))
        val capitals = CapitalLogicModule(territories.map.territories.map { Capital(it) })
        val game = Game(listOf(territories, capitals))

        val defaultLocation = Location(game)

        game.locations.add(defaultLocation)

        val PC = GameCharacter("Melkar the Magnificant", "assets/general/conversation frame.png", false, defaultLocation, game)
        game.addPlayer(PC)
        game.applyTitleToCharacter(capitals.capitals.first().generateCountTitle(), PC)

        return game
    }

}