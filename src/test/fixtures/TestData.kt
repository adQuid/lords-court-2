package test.fixtures

import aibrain.FinishedDeal
import game.GameCharacter
import game.Game
import game.Writ
import game.Location
import shortstate.linetriggers.testTrigger
import game.titlemaker.CookieWorldTitleFactory
import shortstate.ShortStateGame
import shortstate.dialog.LineMemory
import gamelogic.cookieworld.actionTypes.*
import gamelogic.cookieworld.CookieWorld
import shortstate.dialog.linetypes.*
import shortstate.Conversation
import shortstate.ShortGameScene
import shortstate.dialog.GlobalLineTypeFactory
import shortstate.report.DeliciousnessReport
import game.titles.Baker

fun soloTestGame(): Game{
    val cookieLogic = CookieWorld()
    val game = Game(listOf(cookieLogic))

    game.addPlayer(basicMilkman(game, cookieLogic.locations.first()))
    return game
}

fun soloTestGameWithEverythingOnIt(): Game{
    val game = soloTestGame()

    game.players[0].memory.lines.addAll(fullMemory(game.players[0]))
    game.players[0].specialLines.add(testTrigger)

    //If these two aren't equal, then there's probably a type of line we aren't accounting for
    assert(game.players[0].memory.lines.size == GlobalLineTypeFactory.typeMap.size)

    game.actionsByPlayer.put(game.players[0], mutableListOf(WasteTime()))

    return game
}

fun testGameWithTwoLocations(): Game{
    val game = soloTestGame()
    addExtraLocation(game)

    return game
}

fun soloTestShortgame(): ShortStateGame{
    val game = soloTestGame()
    return ShortStateGame(game, game.locations().first())
}

fun soloTestShortgameWithEverythingOnIt(): ShortStateGame{
    val game = soloTestGameWithEverythingOnIt()
    game.addPlayer(basicMilkman(game, game.locations().first()))
    val retval = ShortStateGame(game, game.locations().first())
    retval.shortGameScene = ShortGameScene(listOf(retval.players[0], retval.players[1]), retval.location.rooms[0], Conversation(retval.location.rooms[0], retval.players[0], retval.players[1]))
    return retval
}

fun twoPlayerTestGame(): Game{
    val cookieLogic = CookieWorld()
    val game = Game(listOf(cookieLogic))

    game.addPlayer(basicMilkman(game, cookieLogic.locations().first()))
    game.addPlayer(basicBaker(game, cookieLogic.locations().first()))

    return game
}

fun twoPlayerTestGameResultingInDeliciousness(): Game{
    val game = twoPlayerTestGame()
    return game
}

fun twoPlayerGameWithWrits(): Game{
    val game = twoPlayerTestGame()
    game.players[0].writs.add(Writ("test writ", neutralDeal(game.players), listOf(game.players[0])))
    return game
}

fun twoPlayerShortGame(): ShortStateGame{
    val game = twoPlayerTestGame()
    return ShortStateGame(game, game.locations().first())
}

fun shortGameResultingInDeliciousness(): ShortStateGame{
    val game = twoPlayerTestGameResultingInDeliciousness()
    return ShortStateGame(game, game.locations().first())
}

private fun basicMilkman(game: Game, location: Location): GameCharacter{
    val retval = GameCharacter("player "+game.nextID, "this should never come up in a test", true, location, game)
    game.applyTitleToCharacter(CookieWorldTitleFactory.makeMilkmanTitle(), retval)
    return retval
}

private fun basicBaker(game: Game, location: Location): GameCharacter{
    val retval = GameCharacter("player "+game.nextID, "this should never come up in a test", true, location, game)
    game.applyTitleToCharacter(Baker("test"), retval)
    return retval
}

fun addExtraLocation(game: Game) {
    val extraLocation = Location(0,0)
    val cookieLogic = game.moduleOfType(CookieWorld.type) as CookieWorld
    cookieLogic.locations.add(extraLocation)

    game.addPlayer(basicBaker(game, extraLocation))
}

private fun fullMemory(dealDummy: GameCharacter): List<LineMemory>{
    return listOf(
    LineMemory(AcceptDeal(savableDeal(dealDummy))),
    LineMemory(Announcement(WasteTime())),
    LineMemory(Approve()),
    LineMemory(Disapprove()),
    LineMemory(GiveReport(DeliciousnessReport(Game(listOf(CookieWorld()))))),
    LineMemory(OfferDeal(savableDeal(dealDummy))),
    LineMemory(QuestionOffer(OfferDeal(savableDeal(dealDummy)))),
    LineMemory(RejectDeal(savableDeal(dealDummy))),
    LineMemory(RequestAdviceForDeal(savableDeal(dealDummy))),
    LineMemory(RequestReport(DeliciousnessReport.type)),
    LineMemory(OfferWrit(Writ("test writ", savableDeal(dealDummy), listOf(dealDummy)))),
    LineMemory(SimpleLine("memory ")),
    LineMemory(Farewell())
    )
}

private fun savableDeal(dealDummy: GameCharacter): FinishedDeal {
    return FinishedDeal(mapOf(
        dealDummy to setOf(WasteTime())))
}