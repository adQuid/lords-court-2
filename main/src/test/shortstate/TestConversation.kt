package test.shortstate

import game.Character
import game.Game
import game.Location
import game.action.Action
import game.action.actionTypes.BakeCookies
import shortstate.Conversation
import shortstate.ShortStateGame
import shortstate.dialog.linetypes.Announcement

fun testBasicConvo(){
    val game = Game()
    val location = Location()

    val character1 = Character("person1", "thisdoesn'tmatter", false, location)
    val character2 = Character("person2", "thisdoesn'tmatter", false, location)

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
