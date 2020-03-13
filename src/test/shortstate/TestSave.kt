package test.shortstate

import org.junit.Test
import shortstate.ShortStateGame
import test.fixtures.soloTestShortgame
import test.fixtures.soloTestShortgameWithEverythingOnIt

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