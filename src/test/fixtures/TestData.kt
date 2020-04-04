package test.fixtures

import aibrain.Deal
import aibrain.FinishedDeal
import game.GameCharacter
import game.Game
import game.Writ
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
import shortstate.dialog.Line
import shortstate.report.DeliciousnessReport
import shortstate.report.ReportType

fun soloTestGame(): Game{
    val game = Game()
    val defaultLocation = Location(game)

    game.locations.add(defaultLocation)

    game.addPlayer(basicMilkman("NPC", game, defaultLocation))
    return game
}

fun soloTestGameWithEverythingOnIt(): Game{
    val game = soloTestGame()

    game.players[0].memory.addAll(fullMemory(game.players[0]))

    //If these two aren't equal, then there's probably a type of line we aren't accounting for
    assert(game.players[0].memory.size == GlobalLineTypeFactory.typeMap.size)

    game.actionsByPlayer.put(game.players[0], mutableListOf(WasteTime()))

    return game
}

fun soloTestShortgame(): ShortStateGame{
    val game = soloTestGame()
    return ShortStateGame(game, game.locations[0])
}

fun soloTestShortgameWithEverythingOnIt(): ShortStateGame{
    val game = soloTestGameWithEverythingOnIt()
    game.addPlayer(basicMilkman("Extra NPC", game, game.locations[0]))
    val retval = ShortStateGame(game, game.locations[0])
    retval.shortGameScene = ShortGameScene(listOf(retval.players[0], retval.players[1]), retval.location.rooms[0], Conversation(retval.players[0], retval.players[1]))
    return retval
}

fun twoPlayerTestGame(): Game{
    val game = Game()
    val location = Location(game)

    game.locations.add(location)
    game.addPlayer(basicMilkman("person1", game, location))
    game.addPlayer(basicMilkman("person2", game, location))

    return game
}

fun twoPlayerGameWithWrits(): Game{
    val game = twoPlayerTestGame()
    game.players[0].writs.add(Writ("test writ", neutralDeal(game.players), listOf(game.players[0])))
    return game
}

fun twoPlayerShortGame(): ShortStateGame{
    val game = twoPlayerTestGame()
    return ShortStateGame(game, game.locations[0])
}

private fun basicMilkman(name: String, game: Game, location: Location): GameCharacter{
    val retval = GameCharacter(name, "this should never come up in a test", true, location, game)
    game.applyTitleToCharacter(TitleFactory.makeMilkmanTitle(), retval)
    return retval
}

private fun fullMemory(dealDummy: GameCharacter): List<Memory>{
    return listOf(
    Memory(AcceptDeal(savableDeal(dealDummy))),
    Memory(Announcement(WasteTime())),
    Memory(Approve()),
    Memory(CiteEffect(savableDeal(dealDummy))),
    Memory(Disapprove()),
    Memory(GiveReport(DeliciousnessReport(17.8))),
    Memory(OfferDeal(savableDeal(dealDummy))),
    Memory(QuestionOffer(OfferDeal(savableDeal(dealDummy)))),
    Memory(RejectDeal(savableDeal(dealDummy))),
    Memory(RequestReport(ReportType.DeliciousnessReportType)),
    Memory(OfferWrit(Writ("test writ", savableDeal(dealDummy), listOf(dealDummy))))
    )
}

private fun savableDeal(dealDummy: GameCharacter): FinishedDeal {
    return FinishedDeal(mapOf(
        dealDummy to setOf(WasteTime())))
}