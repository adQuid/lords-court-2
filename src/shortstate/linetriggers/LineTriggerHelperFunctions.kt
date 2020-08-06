package shortstate.linetriggers

import aibrain.Deal
import game.Game
import shortstate.ShortStateCharacter
import shortstate.dialog.Line
import shortstate.dialog.linetypes.OfferDeal
import shortstate.dialog.linetypes.SimpleLine

val neverBeenCalled = { data: MutableMap<String, Any>, game: Game, line: Line?,  me: ShortStateCharacter -> data["calls"] == 0 && line == null}

fun replyWithSimpleLine(text: String): (data: MutableMap<String, Any>, game: Game, line: Line?, me: ShortStateCharacter) -> Line{
    return {data, game, line, me -> SimpleLine(text) }
}

fun replyWithDeal(deal: Deal): (data: MutableMap<String, Any>, game: Game, line: Line?, me: ShortStateCharacter) -> Line{
    return {data, game, line, me -> OfferDeal(deal) }
}