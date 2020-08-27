package scenario.tutorial

import aibrain.Deal
import aibrain.FinishedDeal
import game.Game
import game.Writ
import game.GameCharacter
import gamelogic.government.actionTypes.GiveTerritory
import gamelogic.playerresources.GiveResource
import gamelogic.playerresources.PlayerResourceTypes
import gamelogic.territory.TerritoryLogicModule
import scenario.tutorial.TutorialGameSetup.TUTORIAL_PLAYER_NAME
import shortstate.ShortStateCharacter
import shortstate.dialog.Line
import shortstate.dialog.linetypes.*
import shortstate.linetriggers.*

val approachTestTrigger = LineTrigger(
    "approach",
    neverBeenCalled,
    replyWithSimpleLine("Yo, I'z talking to ya.")
)

val adviceOnBadFishTrade = LineTrigger(
    "badfish", { data, game, line, me, other ->
        line is RequestAdviceForDeal && dealHasBadOfferForFish(line.deal)
    },
    replyWithTreeLine(
        TreeLine(
            "Honestly, I think you miscalculated the market price for fish. This is too generous.",
            TreeLine("How much do you think I should be paying?", SimpleLine("Perhaps you should set the price to 10 gold per unit of fish."))
        )
    )
)
private fun dealHasBadOfferForFish(deal: Deal): Boolean{
    val cost = deal.theActions().entries.filter { it.key.npc == false }.flatMap{it.value}.filter { it is GiveResource && it.resource == PlayerResourceTypes.GOLD_NAME}.firstOrNull()
    val fish = deal.theActions().entries.filter  { it.key.npc }.flatMap{it.value}.filter { it is GiveResource && it.resource == PlayerResourceTypes.FISH_NAME}.firstOrNull()

    if(cost != null && fish != null){
        return (cost as GiveResource).amount * 9 > (fish as GiveResource).amount
    }
    return false
}

val adviceOnDraftingWrit = LineTrigger(
    "fishnowrit", { data, game, line, me, other ->
        line is OfferDeal && other != null && line.AIResponseFunction(me.convoBrain, other!!, game) is AcceptDeal
    },
    replyWithTreeLine(
        TreeLine("That sounds like a good deal to me, lad. You'll need that in writing though before I can sign on to anything.",
            TreeLine("You've got it; I'll be right back with a writ for you to sign.",
                SimpleLine("Looking forward to it, lad!")
            ),
            TreeLine("I'm afraid that my father is out, and won't be back for a while. I can't get you a king's seal.",
                TreeLine("Ohh no no, that's no problem. I don't need a king's seal or anything. You're a prince, after all!",
                    TreeLine("So what do you need from me?",
                        SimpleLine("Just a simple writ, really. Anything that came from your desk at your workroom and your hand will convince people I'm not scamming them...you can write, right?")
                    )
                )
            )
        )
    )
)

val adviseToGetFish = LineTrigger(
    "gogetfish",
    { data, game, line, me, other -> (line == null || line.canChangeTopic()) && data["calls"] == 0 && gameWouldEndWithoutFish(game, me) },
    replyWithSimpleLine("My lord, I'm concerned about food stocks here. We need to trade with the merchants around here to get additional supplies to last until harvest. I've taken the liberty of inviting a Mr. Laerten to your hall. I suggest you speak to him.")
)
private fun gameWouldEndWithoutFish(game: Game, me: ShortStateCharacter): Boolean{
    if(game.turn > 5){
        return false
    }
    if(me.energy > 850 || (me.energy > 400 && game.playerCharacter().writs.isNotEmpty())){
        return false
    }
    if(game.playerCharacter().resources.get(PlayerResourceTypes.GOLD_NAME) == 0){
        return false
    }
    game.endTurn()
    return game.players.filter { it.npc == false && it.resources.get(PlayerResourceTypes.FISH_NAME) > 0 }.isEmpty()
}

val chideForBadDeal = LineTrigger(
    "badfishdeal",
    { data, game, line, me, other -> data["calls"] == 0
        && playerIsPayingTooMuchForFish( game, me.player)
        && (line == null || line.canChangeTopic())
    },
    replyWithSimpleLine("That was a horrible deal!")
)
private fun playerIsPayingTooMuchForFish(game: Game, me: GameCharacter): Boolean{
    val player = game.playerCharacter()
    val merchant = game.players.filter { it.npc && it.resources.get(PlayerResourceTypes.FISH_NAME) > 0 }.firstOrNull()
    if(merchant == null){
        return false
    }
    if(player.writs.filter { it.complete() && it.signatories.contains(merchant)}.isNotEmpty()){
        val writ = player.writs.filter { it.complete() && it.signatories.contains(merchant)}.first()
        if(me.brain.dealValueToCharacter(writ.deal, merchant!!) > 0.2){
            return true
        }else{
            println(me.brain.dealValueToCharacter(writ.deal, merchant))
            return false
        }
    }
    return false
}

val talkToDadTrigger1 = LineTrigger(
    "talktodad1",
    and(
        neverBeenCalled,
        safeForNewTopic,
        talkingToSpecific("Mayren")
    ), replyWithTreeLine(
        TreeLine(
            "Are you hurt, father?",
            SimpleLine("I am well son. And now that I see you are safe, my heart is well too.")
        )
    )
)

val talkToDadTrigger2 = LineTrigger(
    "talktodad2",
    and(
        neverBeenCalled,
        safeForNewTopic,
        talkingToSpecific("Mayren")
    ), replyWithTreeLine(
        TreeLine(
            "Did you fight the enemy? Did we win?",
            TreeLine(
                "We stand victorious. The Grogan were repelled at sea. Not a single Danswadan man lost. If only you had been there to see the men cheer.",
                TreeLine("Glorious!",
                    SimpleLine("Indeed, and now that I've done my part in this war, it's time you do yours.")
                ),
                TreeLine(
                    "If you had sent word where you were, I would have.",
                    TreeLine(
                        "I couldn't risk losing you in the field. I had no way to know the battle would go so well.",
                        TreeLine("I understand.", SimpleLine("I knew you would. You'll have your chance someday.")),
                        TreeLine(
                            "How am I supposed to learn command when you won't let me fight!?",
                            TreeLine("${TUTORIAL_PLAYER_NAME}, you may be my son, but I am still your king. Come back when you have a better attitude.")
                        )
                    )
                )
            )
        )
    )
)

val talkToDadTrigger3 = LineTrigger(
    "talktodad3",
    and(
        neverBeenCalled,
        otherTriggerCalledByPlayer(talkToDadTrigger2),
        safeForNewTopic,
        talkingToSpecific("Mayren")
    ), replyWithTreeLine(
        TreeLine(
            "If I wasn't sent here to help you fight, why am I here?",
            TreeLine(
                "You are here to take command of this town. I will be marching off to counter attack, and God willing end this war. This will leave Port Fog undefended, and I want to show that these people still fall under my protection. Kaireth will stay here to serve as your advisor. Listen to him; he's a smart man.",
                SimpleLine("I won't fail you, father."),
                TreeLine(
                    "You would put your son at risk to prove a point?",
                    TreeLine("You are a prince, and thus you are this kingdom. It is the duty of royalty to stand in the face of your people's danger.",
                        SimpleLine("I understand.")
                    )
                )
            )
        )
    )
)

val talkToDadTrigger4 = LineTrigger(
    "talktodad4",
    and(notStartingConvo, neverBeenCalled, safeForNewTopic, otherTriggerCalledByPlayer(talkToDadTrigger3)),
    grantStartingCounty()
)
fun grantStartingCounty(): (data: MutableMap<String, Any>, game: Game, line: Line?, me: ShortStateCharacter, other: ShortStateCharacter?) -> Line {
    return {data, game, line, me, other ->

        val PC = game.playerCharacter()
        val territoryLogic = game.moduleOfType(TerritoryLogicModule.type) as TerritoryLogicModule
        val territory = territoryLogic.territories().first()
        val deal = FinishedDeal(mapOf(
            me.player to setOf(GiveTerritory(territory.id,PC.id), GiveResource(PC.id, PlayerResourceTypes.GOLD_NAME, 100)),
            PC to setOf(GiveResource(me.player.id, PlayerResourceTypes.GOLD_NAME, 0))
        ))
        val writ = Writ("Transfer of Title to ${PC.fullName()} for Indefinite Period", deal, listOf(me.player))

        OfferWrit(writ).withSpecialText("I march to the Grogan homeland soon. If you would sign the paper, ${territory.name} is yours. (click to see writ details)")
    }
}

val adviseToTalkToDad = LineTrigger(
    "talktodad",
    and(neverBeenCalled, belowEnergy(800), otherTriggerNotCalledByPlayer(talkToDadTrigger1), otherTriggerNotCalledByPlayer(
        talkToDadTrigger2
    )),
    replyWithSimpleLine("Your father is back in town, and you don't know for how long. You should spend more time with him.")
)

val TRIGGER_MAP = listOf(
    approachTestTrigger,
    adviseToTalkToDad,
    adviceOnBadFishTrade,
    adviceOnDraftingWrit,
    adviseToGetFish,
    chideForBadDeal,
    talkToDadTrigger1,
    talkToDadTrigger2,
    talkToDadTrigger3,
    talkToDadTrigger4
).map { it.id to it }.toMap().toMutableMap()
