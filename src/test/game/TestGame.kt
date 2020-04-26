package test.game

import game.Game
import game.logicmodules.CookieWorld
import org.junit.Test
import test.fixtures.soloTestGame
import test.fixtures.soloTestGameWithEverythingOnIt
import test.fixtures.twoPlayerGameWithWrits
import kotlin.test.assertTrue

class TestGame {

    @Test
    fun testGameCreation() {
        val game = Game(listOf(CookieWorld()))
        assertTrue(game.turn == 1)
        assertTrue(game.isLive)
        assertTrue(game.actionsByPlayer.size == 0)
    }

    @Test
    fun testGameEndTurn() {
        val game = Game(listOf(CookieWorld()))
        game.endTurn()
        assertTrue(game.turn == 2)
    }

    @Test
    fun testGameClone() {
        val game = soloTestGameWithEverythingOnIt()
        val game2 = Game(game)
        assert(game2 == game)
    }

    @Test
    fun testSaveGame() {
        testSaveHelper(soloTestGame())
        testSaveHelper(soloTestGameWithEverythingOnIt())
        testSaveHelper(twoPlayerGameWithWrits())
    }

    private fun testSaveHelper(game: Game){
        println(game.saveString())
        val game2 = Game(game.saveString())
        assertTrue(game == game2)
        assertTrue(game.players[0].acceptedDeals == game2.players[0].acceptedDeals)
        assertTrue(game.titles.size == game2.titles.size)
        assertTrue(game.titles.toList()[0] == game2.titles.toList()[0])
        game.players.forEach { player ->
            assertTrue { player.name == game2.characterById(player.id).name };
            assertTrue { player.titles.size == game2.characterById(player.id).titles.size }
            assertTrue { player.memory.lines.size == game2.characterById(player.id).memory.lines.size }
            player.memory.lines.forEachIndexed { index, memory -> memory.line.type == (game2.characterById(player.id).memory.lines[index].line.type) }
        }
    }

}