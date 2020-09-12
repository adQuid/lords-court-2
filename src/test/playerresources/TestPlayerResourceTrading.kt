package test.playerresources

import game.GameCharacter
import game.action.Action
import gamelogic.playerresources.GiveResource
import gamelogic.playerresources.PlayerResourceTypes
import gamelogic.resources.ResourceTypes
import org.junit.Test
import test.fixtures.soloTestGame
import test.fixtures.soloTestResourceGame

class TestPlayerResourceTrading {

    @Test
    fun testTrade(){
        val testGame = soloTestResourceGame()
        testGame.addPlayer(GameCharacter("player "+testGame.nextID, "this should never come up in a test", true, testGame.locations().first(), testGame))

        testGame.players[0].privateResources.set(ResourceTypes.FISH_NAME, 100)

        testGame.actionsByPlayer[testGame.players[0]] = listOf<Action>(GiveResource(testGame.players[1].id, ResourceTypes.FISH_NAME, 25)).toMutableList()

        testGame.endTurn()

        assert(testGame.players[0].privateResources.get(ResourceTypes.FISH_NAME) == 75)
        assert(testGame.players[1].privateResources.get(ResourceTypes.FISH_NAME) == 25)
    }

}