package test.game

import game.Game
import kotlin.test.assertTrue

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