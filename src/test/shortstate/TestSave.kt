package test.shortstate

import org.junit.Test
import shortstate.ShortStateGame
import test.util.soloTestGame
import test.util.soloTestShortgame
import test.util.soloTestShortgameWithEverythingOnIt

class TestSave {

    @Test
    fun testSaveShortGame(){
        shortStateTestHelper(soloTestShortgame())
        shortStateTestHelper(soloTestShortgameWithEverythingOnIt())
    }

    private fun shortStateTestHelper(shortGame: ShortStateGame){
        val shortGame2 = ShortStateGame(shortGame.game, shortGame.saveString())
        assert(shortGame == shortGame2)
    }

}