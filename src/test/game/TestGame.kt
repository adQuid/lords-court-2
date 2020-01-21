package test.game

import game.Game
import org.junit.Test
import test.util.soloTestGame
import kotlin.test.assertTrue

class TestGame {

    @Test
    fun testGameCreation() {
        val game = Game()
        assertTrue(game.turn == 1)
        assertTrue(game.isLive)
        assertTrue(game.actionsByPlayer.size == 0)
    }

    @Test
    fun testGameEndTurn() {
        val game = Game()
        game.endTurn()
        assertTrue(game.turn == 2)
    }

    @Test
    fun testSaveGame() {
        val game = soloTestGame()
        val game2 = Game(game.saveString())
        assertTrue(game == game2)
        assertTrue(game.players[0].acceptedDeals == game2.players[0].acceptedDeals)
    }

}