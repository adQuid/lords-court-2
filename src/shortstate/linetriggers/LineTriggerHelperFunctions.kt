package shortstate.linetriggers

import aibrain.Deal
import game.Game
import shortstate.ShortStateCharacter
import shortstate.dialog.Line
import shortstate.dialog.linetypes.OfferDeal
import shortstate.dialog.linetypes.SimpleLine
import shortstate.dialog.linetypes.TreeLine

val neverBeenCalled = { data: MutableMap<String, Any>, game: Game, line: Line?,  me: ShortStateCharacter, other: ShortStateCharacter? -> data["calls"] == 0}

val safeForNewTopic = { data: MutableMap<String, Any>, game: Game, line: Line?,  me: ShortStateCharacter, other: ShortStateCharacter? -> (line == null || line.canChangeTopic())}

fun and(vararg functions: (data: MutableMap<String, Any>, game: Game, line: Line?, me: ShortStateCharacter, other: ShortStateCharacter?) -> Boolean): (data: MutableMap<String, Any>, game: Game, line: Line?, me: ShortStateCharacter, other: ShortStateCharacter?) -> Boolean{
    return {data, game, line, me, other -> functions.filter{!it(data, game, line, me, other)}.isEmpty()}
}

fun talkingToSpecific(text: String): (data: MutableMap<String, Any>, game: Game, line: Line?, me: ShortStateCharacter, other: ShortStateCharacter?) -> Boolean{
    return {data, game, line, me, other -> other != null && other.player.name == text }
}

fun otherTriggerCalled(trigger: LineTrigger): (data: MutableMap<String, Any>, game: Game, line: Line?, me: ShortStateCharacter, other: ShortStateCharacter?) -> Boolean{
    return {data, game, line, me, other -> me.player.specialLines.filter { it.id == trigger.id }.first().data["calls"] as Int > 0 }
}

fun replyWithSimpleLine(text: String): (data: MutableMap<String, Any>, game: Game, line: Line?, me: ShortStateCharacter, other: ShortStateCharacter?) -> Line{
    return {data, game, line, me, other -> SimpleLine(text) }
}

fun replyWithTreeLine(myline: TreeLine): (data: MutableMap<String, Any>, game: Game, line: Line?, me: ShortStateCharacter, other: ShortStateCharacter?) -> Line{
    return {data, game, line, me, other -> myline }
}

fun replyWithDeal(deal: Deal): (data: MutableMap<String, Any>, game: Game, line: Line?, me: ShortStateCharacter, other: ShortStateCharacter?) -> Line{
    return {data, game, line, me, other -> OfferDeal(deal) }
}