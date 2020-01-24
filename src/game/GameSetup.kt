package game

import game.titlemaker.TitleFactory

class GameSetup {

    fun setupGame(game: Game){
        val defaultLocation = Location(game)

        game.locations.add(defaultLocation)

        val PC = GameCharacter("Melkar the Magnificant", "assets//general//conversation frame.png", false, defaultLocation, game)
        game.applyTitleToCharacter(TitleFactory.makeCountTitle("Cookies"), PC)
        game.players.add(PC)

        val NPC = GameCharacter("Frip", "assets//portraits//faceman.png", true, defaultLocation, game)
        NPC.titles.add(TitleFactory.makeCountTitle("Eating Cookies"))
        NPC.titles.add(TitleFactory.makeCountTitle("Nomz, munch, and food-related goodness"))
        game.players.add(NPC)
    }

}