package test.playerresources

import game.GameCharacter
import game.action.Action
import gamelogic.playerresources.GiveResource
import gamelogic.playerresources.PlayerResourceTypes
import org.junit.Test
import test.fixtures.soloTestGame

class TestPlayerResourceTrading {

    @Test
    fun testTrade(){
        val testGame = soloTestGame()
        testGame.addPlayer(GameCharacter("player "+testGame.nextID, "this should never come up in a test", true, testGame.locations().first(), testGame))

        testGame.players[0].resources.set(PlayerResourceTypes.FISH_NAME, 100)

        testGame.actionsByPlayer[testGame.players[0]] = listOf<Action>(GiveResource(testGame.players[1].id, PlayerResourceTypes.FISH_NAME, 25)).toMutableList()

        testGame.endTurn()

        assert(testGame.players[0].resources.get(PlayerResourceTypes.FISH_NAME) == 75)
        assert(testGame.players[1].resources.get(PlayerResourceTypes.FISH_NAME) == 25)
    }

}