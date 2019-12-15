package game

import game.titlemaker.TitleFactory

class GameSetup {

    fun setupGame(game: Game){
        val defaultLocation = Location()

        game.locations.add(defaultLocation)

        val PC = Character("Melkar the Magnificant", "assets//general//conversation frame.png", false, defaultLocation)
        game.applyTitleToCharacter(TitleFactory.makeCountTitle("Cookies"), PC)
        game.players.add(PC)

        val NPC = Character("Frip", "assets//portraits//faceman.png", true, defaultLocation)
        NPC.titles.add(TitleFactory.makeCountTitle("Eating Cookies"))
        NPC.titles.add(TitleFactory.makeCountTitle("Nomz, munch, and food-related goodness"))
        game.players.add(NPC)
    }

}