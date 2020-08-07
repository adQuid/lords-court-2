package shortstate.linetriggers

import aibrain.Deal
import game.Game
import shortstate.ShortStateCharacter
import shortstate.dialog.Line
import shortstate.dialog.linetypes.OfferDeal
import shortstate.dialog.linetypes.SimpleLine
import shortstate.dialog.linetypes.TreeLine

val neverBeenCalled = { data: MutableMap<String, Any>, game: Game, line: Line?,  me: ShortStateCharacter -> data["calls"] == 0 && line == null}

fun replyWithSimpleLine(text: String): (data: MutableMap<String, Any>, game: Game, line: Line?, me: ShortStateCharacter) -> Line{
    return {data, game, line, me -> SimpleLine(text) }
}

fun replyWithTreeLine(myline: TreeLine): (data: MutableMap<String, Any>, game: Game, line: Line?, me: ShortStateCharacter) -> Line{
    return {data, game, line, me -> myline }
}

fun replyWithDeal(deal: Deal): (data: MutableMap<String, Any>, game: Game, line: Line?, me: ShortStateCharacter) -> Line{
    return {data, game, line, me -> OfferDeal(deal) }
}