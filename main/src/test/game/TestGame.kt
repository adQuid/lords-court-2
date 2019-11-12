package test.game

import game.Game
import test.myAssert

fun testGameCreation() {
    val game = Game()
    myAssert(game.turn == 1)
    myAssert(game.isLive)
    myAssert(game.actionsByPlayer.size == 0)
}

fun testGameEndTurn() {
    val game = Game()
    game.endTurn()
    myAssert(game.turn == 2)
}