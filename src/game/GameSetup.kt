package game

import game.titlemaker.CookieWorldTitleFactory

class GameSetup {

    fun setupGame(game: Game){
        val defaultLocation = Location(game)

        game.locations.add(defaultLocation)

        val PC = GameCharacter("Melkar the Magnificant", "assets/general/conversation frame.png", false, defaultLocation, game)
        game.applyTitleToCharacter(CookieWorldTitleFactory.makeBakerTitle("Cookies"), PC)
        game.addPlayer(PC)

        val NPC = GameCharacter("Frip", "assets/portraits/faceman.png", true, defaultLocation, game)
        NPC.titles.add(CookieWorldTitleFactory.makeMilkmanTitle())
        game.addPlayer(NPC)
    }

}