package test.shortstate

import shortstate.Conversation
import shortstate.dialog.linetypes.Announcement
import game.action.Action
import game.action.actionTypes.BakeCookies
import test.fixtures.twoPlayerShortGame
import org.junit.Test

class TestConversation {

    @Test
    fun testBasicConvo(){
        val shortGame = twoPlayerShortGame()

        val convo = Conversation(shortGame.players[0], shortGame.players[1])

        val line1 = Announcement(Action(BakeCookies()))
        convo.submitLine(line1, shortGame)
        val line2 = line1.possibleReplies()[0]
        convo.submitLine(line2, shortGame)
    }
}