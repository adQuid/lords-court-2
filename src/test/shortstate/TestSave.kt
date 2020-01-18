package test.shortstate

import org.junit.Test
import shortstate.ShortStateGame
import test.util.soloTestGame

class TestSave {

    @Test
    fun testSaveShortGame(){
        val game = soloTestGame()
        val shortGame1 = ShortStateGame(game, game.locations[0])

        val shortGame2 = ShortStateGame(game, shortGame1.saveString())

        assert(shortGame1 == shortGame2)
    }

}