package test.game

import game.Game
import kotlin.test.*
import test.util.soloTestGame

fun testGameCreation() {
    val game = Game()
    assertTrue(game.turn == 1)
    assertTrue(game.isLive)
    assertTrue(game.actionsByPlayer.size == 0)
}

fun testGameEndTurn() {
    val game = Game()
    game.endTurn()
    assertTrue(game.turn == 2)
}

@Test fun testSaveGame() {
    val game = soloTestGame()
    val game2 = Game(game.saveString())
    assertTrue(game == game2)
}