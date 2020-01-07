package test.util

import game.Character
import game.Game
import game.Location
import game.titlemaker.TitleFactory

fun soloTestGame(): Game{
    val game = Game()
    val defaultLocation = Location(game)

    game.locations.add(defaultLocation)

    val player1 = Character("NPC 1", "this should never come up in a test", true, defaultLocation, game)
    game.applyTitleToCharacter(TitleFactory.makeCountTitle("Cookies"), player1)
    game.players.add(player1)

    return game
}