package test.fixtures

import aibrain.FinishedDeal
import game.GameCharacter
import game.Game
import game.Writ
import game.Location
import game.titlemaker.CookieWorldTitleFactory
import shortstate.ShortStateGame
import shortstate.dialog.LineMemory
import gamelogicmodules.cookieworld.actionTypes.*
import gamelogicmodules.cookieworld.CookieWorld
import shortstate.dialog.linetypes.*
import shortstate.Conversation
import shortstate.ShortGameScene
import shortstate.dialog.GlobalLineTypeFactory
import shortstate.report.DeliciousnessReport
import game.titles.Baker

fun soloTestGame(): Game{
    val game = Game(listOf(CookieWorld()))
    val defaultLocation = Location(game)

    game.locations.add(defaultLocation)

    game.addPlayer(basicMilkman(game, defaultLocation))
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

fun testGameWithTwoLocations(): Game{
    val game = soloTestGame()
    addExtraLocation(game)

    return game
}

fun soloTestShortgame(): ShortStateGame{
    val game = soloTestGame()
    return ShortStateGame(game, game.locations[0])
}

fun soloTestShortgameWithEverythingOnIt(): ShortStateGame{
    val game = soloTestGameWithEverythingOnIt()
    game.addPlayer(basicMilkman(game, game.locations[0]))
    val retval = ShortStateGame(game, game.locations[0])
    retval.shortGameScene = ShortGameScene(listOf(retval.players[0], retval.players[1]), retval.location.rooms[0], Conversation(retval.location.rooms[0], retval.players[0], retval.players[1]))
    return retval
}

fun twoPlayerTestGame(): Game{
    val game = Game(listOf(CookieWorld()))
    val location = Location(game)

    game.locations.add(location)
    game.addPlayer(basicMilkman(game, location))
    game.addPlayer(basicBaker(game, location))

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
    return ShortStateGame(game, game.locations[0])
}

fun shortGameResultingInDeliciousness(): ShortStateGame{
    val game = twoPlayerTestGameResultingInDeliciousness()
    return ShortStateGame(game, game.locations[0])
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
    val extraLocation = Location(game)
    game.locations.add(extraLocation)

    game.addPlayer(basicBaker(game, extraLocation))
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
    LineMemory(RequestAdviceForDeal(savableDeal(dealDummy))),
    LineMemory(RequestReport(DeliciousnessReport.type)),
    LineMemory(OfferWrit(Writ("test writ", savableDeal(dealDummy), listOf(dealDummy)))),
    LineMemory(Farewell())
    )
}

private fun savableDeal(dealDummy: GameCharacter): FinishedDeal {
    return FinishedDeal(mapOf(
        dealDummy to setOf(WasteTime())))
}