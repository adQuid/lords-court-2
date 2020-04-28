package test.fixtures

import aibrain.FinishedDeal
import game.GameCharacter
import game.Game
import game.Writ
import game.Location
import game.titlemaker.TitleFactory
import shortstate.ShortStateGame
import shortstate.dialog.LineMemory
import game.action.actionTypes.*
import game.gamelogicmodules.CookieWorld
import shortstate.dialog.linetypes.*
import shortstate.Conversation
import shortstate.ShortGameScene
import shortstate.dialog.GlobalLineTypeFactory
import shortstate.report.DeliciousnessReport
import shortstate.report.ReportType
import game.titles.Milkman
import game.titles.Baker

fun soloTestGame(): Game{
    val game = Game(listOf(CookieWorld()))
    val defaultLocation = Location(game)

    game.locations.add(defaultLocation)

    game.addPlayer(basicMilkman("NPC", game, defaultLocation))
    return game
}

fun soloTestGameWithEverythingOnIt(): Game{
    val game = soloTestGame()

    game.players[0].memory.lines.addAll(fullMemory(game.players[0]))

    //If these two aren't equal, then there's probably a type of line we aren't accounting for
    assert(game.players[0].memory.lines.size == GlobalLineTypeFactory.typeMap.size)

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
    retval.shortGameScene = ShortGameScene(listOf(retval.players[0], retval.players[1]), retval.location.rooms[0], Conversation(retval.location.rooms[0], retval.players[0], retval.players[1]))
    return retval
}

fun twoPlayerTestGame(): Game{
    val game = Game(listOf(CookieWorld()))
    val location = Location(game)

    game.locations.add(location)
    game.addPlayer(basicMilkman("person1", game, location))
    game.addPlayer(basicMilkman("person2", game, location))

    return game
}

fun twoPlayerTestGameResultingInDeliciousness(): Game{
    val game = twoPlayerTestGame()
    game.applyTitleToCharacter(Milkman(), game.players[0])
    game.applyTitleToCharacter(Baker("test"), game.players[1])
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

fun shortGameResultingInDeliciousness(): ShortStateGame{
    val game = twoPlayerTestGameResultingInDeliciousness()
    return ShortStateGame(game, game.locations[0])
}

private fun basicMilkman(name: String, game: Game, location: Location): GameCharacter{
    val retval = GameCharacter(name, "this should never come up in a test", true, location, game)
    game.applyTitleToCharacter(TitleFactory.makeMilkmanTitle(), retval)
    return retval
}

private fun fullMemory(dealDummy: GameCharacter): List<LineMemory>{
    return listOf(
    LineMemory(AcceptDeal(savableDeal(dealDummy))),
    LineMemory(Announcement(WasteTime())),
    LineMemory(Approve()),
    LineMemory(CiteEffect(savableDeal(dealDummy))),
    LineMemory(Disapprove()),
    LineMemory(GiveReport(DeliciousnessReport(Game(listOf(CookieWorld()))))),
    LineMemory(OfferDeal(savableDeal(dealDummy))),
    LineMemory(QuestionOffer(OfferDeal(savableDeal(dealDummy)))),
    LineMemory(RejectDeal(savableDeal(dealDummy))),
    LineMemory(RequestReport(DeliciousnessReport.type)),
    LineMemory(OfferWrit(Writ("test writ", savableDeal(dealDummy), listOf(dealDummy)))),
    LineMemory(Farewell())
    )
}

private fun savableDeal(dealDummy: GameCharacter): FinishedDeal {
    return FinishedDeal(mapOf(
        dealDummy to setOf(WasteTime())))
}