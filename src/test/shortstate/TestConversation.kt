package test.shortstate

import game.Game
import shortstate.Conversation
import shortstate.ShortStateGame
import shortstate.dialog.linetypes.Announcement
import game.action.Action
import game.Location
import game.action.actionTypes.BakeCookies
import game.GameCharacter
import org.junit.Test

class TestConversation {

    @Test
    fun testBasicConvo(){
        val game = Game()
        val location = Location(game)

        val character1 = GameCharacter("person1", "thisdoesn'tmatter", false, location, game)
        val character2 = GameCharacter("person2", "thisdoesn'tmatter", false, location, game)

        game.locations.add(location)
        game.players.add(character1)
        game.players.add(character2)

        val shortGame = ShortStateGame(game, location)

        val convo = Conversation(shortGame.players[0], shortGame.players[1])

        val line1 = Announcement(Action(BakeCookies()))
        convo.submitLine(line1, shortGame)
        val line2 = line1.possibleReplies()[0]
        convo.submitLine(line2, shortGame)
    }
}