package test.game

import game.Game
import org.junit.Test
import test.util.soloTestGame
import test.util.soloTestGameWithEverythingOnIt
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
        testSaveHelper(soloTestGame())
        testSaveHelper(soloTestGameWithEverythingOnIt())
    }

    private fun testSaveHelper(game: Game){
        val game2 = Game(game.saveString())
        assertTrue(game == game2)
        assertTrue(game.players[0].acceptedDeals == game2.players[0].acceptedDeals)
        game.players.forEach { player ->
            assertTrue { player.name == game2.characterById(player.id).name };
            assertTrue { player.titles.size == game2.characterById(player.id).titles.size }
            assertTrue { player.memory.size == game2.characterById(player.id).memory.size }
            player.memory.forEachIndexed { index, memory -> memory.line.type == (game2.characterById(player.id).memory[index].line.type) }
        }
    }

}