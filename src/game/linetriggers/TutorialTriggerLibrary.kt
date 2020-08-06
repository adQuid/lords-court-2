package game.linetriggers

import aibrain.Deal
import gamelogic.playerresources.GiveResource
import gamelogic.playerresources.PlayerResourceTypes
import shortstate.dialog.linetypes.RequestAdviceForDeal
import shortstate.dialog.linetypes.SimpleLine

val testTrigger = LineTrigger("test", { data, game, line, me ->
    if(me.player.memory.lines.size < 2){ true } else { false }
},
    {data, game, line, me -> SimpleLine("test") }
)

val adviceOnBadFishTrade = LineTrigger("badfish", { data, game, line, me ->
    line is RequestAdviceForDeal && dealHasBadOfferForFish(line.deal)}, {data, game, line, me -> SimpleLine("Your idea is bad, and you should feel bad.") } )
private fun dealHasBadOfferForFish(deal: Deal): Boolean{
    val cost = deal.theActions().entries.filter { it.key.npc == false }.flatMap{it.value}.filter { it is GiveResource && it.resource == PlayerResourceTypes.GOLD_NAME}.firstOrNull()
    val fish = deal.theActions().entries.filter  { it.key.npc }.flatMap{it.value}.filter { it is GiveResource && it.resource == PlayerResourceTypes.FISH_NAME}.firstOrNull()

    if(cost != null && fish != null){
        return (cost as GiveResource).amount > (fish as GiveResource).amount * 12
    }
    return false
}

val approachTestTrigger = LineTrigger("approach", { data, game, line, me -> true}, {data, game, line, me -> SimpleLine("yo, I'z talkin to ya") } )

val TRIGGER_MAP = listOf(approachTestTrigger, adviceOnBadFishTrade, testTrigger).map { it.id to it }.toMap().toMutableMap()

fun triggerFromSaveString(saveString: Map<String, Any>): LineTrigger{
    val retval = TRIGGER_MAP[saveString["type"] as String]!!
    retval.data = (saveString["data"] as Map<String, Any>).toMutableMap()
    return retval
}