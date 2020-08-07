package shortstate.linetriggers

import aibrain.Deal
import game.Game
import game.GameCharacter
import gamelogic.playerresources.GiveResource
import gamelogic.playerresources.PlayerResourceTypes
import shortstate.ShortStateCharacter
import shortstate.dialog.linetypes.RequestAdviceForDeal
import shortstate.dialog.linetypes.SimpleLine
import shortstate.dialog.linetypes.TreeLine

val testTrigger = LineTrigger("test", { data, game, line, me, other ->
    if(me.player.memory.lines.size < 2){ true } else { false }
},
    {data, game, line, me, other -> SimpleLine("test") }
)

val adviceOnBadFishTrade = LineTrigger("badfish", { data, game, line, me, other ->
    line is RequestAdviceForDeal && dealHasBadOfferForFish(line.deal)},
    replyWithTreeLine(TreeLine("Your idea is bad, and you should feel bad.",TreeLine("How much do you think I should be paying?", SimpleLine("Maybe like 10 fish per gold?")))) )
private fun dealHasBadOfferForFish(deal: Deal): Boolean{
    val cost = deal.theActions().entries.filter { it.key.npc == false }.flatMap{it.value}.filter { it is GiveResource && it.resource == PlayerResourceTypes.GOLD_NAME}.firstOrNull()
    val fish = deal.theActions().entries.filter  { it.key.npc }.flatMap{it.value}.filter { it is GiveResource && it.resource == PlayerResourceTypes.FISH_NAME}.firstOrNull()

    if(cost != null && fish != null){
        return (cost as GiveResource).amount * 12 > (fish as GiveResource).amount
    }
    return false
}

val adviseToGetFish = LineTrigger("gogetfish", { data, game, line, me, other -> data["calls"] == 0 && gameWouldEndWithoutFish(game, me)}, replyWithSimpleLine("You fool, get some fish!"))
private fun gameWouldEndWithoutFish(game: Game, me: ShortStateCharacter): Boolean{
    if(me.energy > 850 || (me.energy > 400 && game.playerCharacter().writs.isNotEmpty())){
        return false
    }
    game.endTurn()
    return game.players.filter { it.resources.get(PlayerResourceTypes.FISH_NAME) > 0 }.size < 2
}

val chideForBadDeal = LineTrigger("badfishdeal", { data, game, line, me, other -> data["calls"] == 0 && playerIsPayingTooMuchForFish(game, me.player)}, replyWithSimpleLine("That was a horrible deal!"))
private fun playerIsPayingTooMuchForFish(game: Game, me: GameCharacter): Boolean{
    val player = game.playerCharacter()
    val merchant = game.players.filter { it.npc && it.resources.get(PlayerResourceTypes.FISH_NAME) > 0 }.first()
    if(player.writs.filter { it.complete() && it.signatories.contains(merchant)}.isNotEmpty()){
        val writ = player.writs.filter { it.complete() && it.signatories.contains(merchant)}.first()
        if(me.brain.dealValueToCharacter(writ.deal, merchant) > 0.2){
            return true
        }else{
            println(me.brain.dealValueToCharacter(writ.deal, merchant))
            return false
        }
    }
    return false
}

val approachTestTrigger = LineTrigger("approach", neverBeenCalled, replyWithSimpleLine("Yo, I'z talking to ya.") )

val talkToDadTrigger = LineTrigger("talktodad", talkingToSpecific("Mayren"), replyWithSimpleLine("Whazup?"))

val TRIGGER_MAP = listOf(approachTestTrigger, adviceOnBadFishTrade, adviseToGetFish, chideForBadDeal, talkToDadTrigger, testTrigger).map { it.id to it }.toMap().toMutableMap()
