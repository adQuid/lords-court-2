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

val talkToDadTrigger1 = LineTrigger("talktodad1",
    and(neverBeenCalled,talkingToSpecific("Mayren")), replyWithTreeLine(
        TreeLine("Are you hurt, father?",
            SimpleLine("I am well son. And now that I see you are safe, my heart is well too."))))

val talkToDadTrigger2 = LineTrigger("talktodad2",
    and(neverBeenCalled,safeForNewTopic,talkingToSpecific("Mayren")), replyWithTreeLine(
    TreeLine("Did you fight the enemy? Did we win?",
        TreeLine("We stand victorious. The Grogan were repelled at sea. Not a single Danswadan man lost. If only you had been there to see the men cheer.",
            TreeLine("Glorious!", SimpleLine("Which is where you come in..")),
            TreeLine("If you had sent word where you were, I would have.",
                TreeLine("I couldn't risk losing you in the field. I had no way to know the battle would go so well.",
                    TreeLine("I understand.", SimpleLine("I knew you would. You'll have your chance someday.")),
                    TreeLine("How am I supposed to learn command when you won't let me fight!?", TreeLine("Melkar, you may be my son, but I am still your king. Come back when you have a better attitude."))
                )
            )
        )
    )))

val talkToDadTrigger3 = LineTrigger("talktodad3",
    and(neverBeenCalled, otherTriggerCalled(talkToDadTrigger2),safeForNewTopic,talkingToSpecific("Mayren")), replyWithTreeLine(
        TreeLine("If I wasn't sent here to help you fight, why am I here?",
            TreeLine("You are here to take command of this town. Our armies will be marching off to counter attack, and with God's grace end this war. This will leave Port Fog undefended, and I want to show that these people still fall under my protection.",
                TreeLine("You would put your son at risk to prove a point?",
                    SimpleLine("You are a prince, and thus you are this kingdom. It is the duty of royalty to stand in the face of your people's danger.")
                )
            )
        )
    ))

val TRIGGER_MAP = listOf(approachTestTrigger, adviceOnBadFishTrade, adviseToGetFish, chideForBadDeal, talkToDadTrigger1, talkToDadTrigger2, talkToDadTrigger3).map { it.id to it }.toMap().toMutableMap()
