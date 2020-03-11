package test.util

import aibrain.Deal
import aibrain.FinishedDeal
import game.GameCharacter
import game.Game
import game.Location
import game.titlemaker.TitleFactory
import shortstate.ShortStateGame
import shortstate.dialog.Memory
import game.action.actionTypes.*
import game.action.Action
import shortstate.dialog.linetypes.*
import game.effects.AddDelicousness
import shortstate.Conversation
import shortstate.ShortGameScene
import shortstate.dialog.GlobalLineTypeFactory
import shortstate.report.DeliciousnessReport
import shortstate.report.ReportType

fun soloTestGame(): Game{
    val game = Game()
    val defaultLocation = Location(game)

    game.locations.add(defaultLocation)

    val player1 = GameCharacter("NPC 1", "this should never come up in a test", true, defaultLocation, game)
    game.applyTitleToCharacter(TitleFactory.makeCountTitle("Cookies"), player1)
    game.players.add(player1)

    return game
}

fun soloTestGameWithEverythingOnIt(): Game{
    val game = soloTestGame()
    game.players[0].memory.add(Memory(AcceptDeal(FinishedDeal(mapOf(
        game.players[0] to listOf(Action(WasteTime()))
    ))
    )))
    game.players[0].memory.add(Memory(Announcement(Action(WasteTime()))))
    game.players[0].memory.add(Memory(Approve()))
    game.players[0].memory.add(Memory(CiteEffect(listOf(AddDelicousness(1.0)))))
    game.players[0].memory.add(Memory(Disapprove()))
    game.players[0].memory.add(Memory(GiveReport(DeliciousnessReport(17.8))))
    game.players[0].memory.add(Memory(OfferDeal(FinishedDeal(mapOf(
        game.players[0] to listOf(Action(WasteTime()))
    )))))
    game.players[0].memory.add(Memory(QuestionSuggestion(SuggestAction(Action(WasteTime())))))
    game.players[0].memory.add(Memory(RejectDeal(FinishedDeal(mapOf(
        game.players[0] to listOf(Action(WasteTime()))
    )))))
    game.players[0].memory.add(Memory(RequestReport(ReportType.DeliciousnessReportType)))
    game.players[0].memory.add(Memory(SuggestAction(Action(WasteTime()))))

    //If these two aren't equal, then there's probably a type of line we aren't accounting for
    assert(game.players[0].memory.size == GlobalLineTypeFactory.typeMap.size)

    game.actionsByPlayer.put(game.players[0], listOf(Action(WasteTime())))

    return game
}

fun soloTestShortgame(): ShortStateGame{
    val game = soloTestGame()
    return ShortStateGame(game, game.locations[0])
}

fun soloTestShortgameWithEverythingOnIt(): ShortStateGame{
    val game = soloTestGameWithEverythingOnIt()
    game.players.add(GameCharacter("NPC 2", "this should never come up in a test", true, game.locations[0], game))
    val retval = ShortStateGame(game, game.locations[0])
    retval.shortGameScene = ShortGameScene(listOf(retval.players[0], retval.players[1]), retval.location.rooms[0], Conversation(retval.players[0], retval.players[1]))
    return retval
}