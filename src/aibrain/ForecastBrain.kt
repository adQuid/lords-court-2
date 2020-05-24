package aibrain

import game.action.Action
import game.Effect
import game.Game
import game.GameCharacter
import game.action.actionTypes.BakeCookies
import game.action.actionTypes.GetMilk
import game.action.actionTypes.WasteTime
import gamelogicmodules.CookieWorld
import main.Controller
import shortstate.dialog.LineMemory
import shortstate.dialog.linetypes.Announcement
import util.safeSublist

class ForecastBrain {
    val player: GameCharacter

    var lastGameIEvaluated: Game? = null

    var maxPlayersToThinkAbout = 3

    //TODO: Why do both of these exist?
    var lastCasesOfConcern: List<GameCase>? = null
    var dealsILike: Map<FinishedDeal, Double>? = null
        get() {if(field == null){thinkAboutNextTurn(Controller.singleton!!.game!!)}; return field}

    var lastFavoriteEffects: List<Effect>? = null
    var lastActionsToCommitTo: List<Action>? = null


    constructor(player: GameCharacter){
        this.player = player
    }

    fun thinkAboutNextTurn(game: Game){
        if(lastGameIEvaluated == null || game != lastGameIEvaluated){
            thinkAboutNextTurnHelper(game)
        }
    }

    private fun thinkAboutNextTurnHelper(game: Game){
        var myGame = game.imageFor(player)
        lastCasesOfConcern = casesOfConcern(myGame).toList().sortedBy { case -> -case.valueToCharacter(player) }
        lastFavoriteEffects = favoriteEffects()
        lastActionsToCommitTo = actionsToDo(myGame)

        //DEBUG
        if(this.player.name == "Frip"){
            println("${player.name} thinks about $lastCasesOfConcern")
            println("${player.name} wants to $lastActionsToCommitTo")
        }

        dealsILike = dealsILike(myGame)
        lastGameIEvaluated = myGame
    }

    private fun favoriteEffects(): List<Effect>{
        var cases = lastCasesOfConcern

        val averageScore = cases!!.map{case -> case.valueToCharacter(player)}.average()

        //map cases to how different they are from average
        val modCases = HashMap<GameCase, Double>()
        cases.forEach { case -> modCases[case] = (case.valueToCharacter(player) - averageScore) * case.probability() }

        var orderedCaseList = modCases.toList().sortedBy { (key, value) -> -value }

        val bestCase = orderedCaseList[0].first

        return bestCase.finalEffects.toMutableList()
    }

    private fun dealsILike(game: Game): Map<FinishedDeal, Double> {
        return mostSignificantPlayersToMe(game).filter { it -> it != player }
            .map { character -> prospectiveDealsWithPlayer(game, character) }.flatten()
            .filter { dealValueToMe(it) > 0 }.associate { deal -> deal to dealValueToMe(deal) }
    }

    private fun actionsToDo(game: Game): List<Action> {
        return lastCasesOfConcern!!.filter { case -> case.plan.player == player }.toList().sortedBy { case -> -case.valueToCharacter(player) }[0].plan.actions
    }

    private fun casesOfConcern(game: Game): List<GameCase>{
        val topPlayers = safeSublist(mostSignificantPlayersToMe(game),0,maxPlayersToThinkAbout)

        val likelyPlans = HashMap<GameCharacter, List<Plan>>()
        topPlayers.forEach{player ->
            likelyPlans[player] = actionPossibilitiesForPlayer(game, player)
        }

        var retval = mutableListOf<GameCase>()
        likelyPlans.keys.forEach {curPlayer ->
            var effects = mutableListOf<Effect>()
            //for every player that isn't this one, add the effect of every action of every plan, layered by the probability of that plan
            for(otherPlayer in likelyPlans.keys.filter{other -> other != curPlayer}) {
                for (plan in likelyPlans[otherPlayer]!!) {
                    for (action in plan.actions) {
                        effects.addAll(
                            action.doAction(game,plan.player).map { effect -> effect.layerProbability(plan.probability) })
                    }
                }
            }

            //make a gamecase for every plan of this player with effects added for the average of what everyone else might do?
            likelyPlans[curPlayer]!!.forEach {
                val toAdd = GameCase(game, it, effects)
                retval.add(toAdd)
            }
        }

        return retval
    }

    private fun prospectiveDealsWithPlayer(game: Game, target: GameCharacter): List<FinishedDeal>{

        val retval = mutableListOf<FinishedDeal>()

        if(game.gameLogicModules.filter { it is CookieWorld }.isNotEmpty()){
            retval.add(FinishedDeal(hashMapOf(
                player to setOf(WasteTime()),
                target to setOf(WasteTime())
            )))

            retval.add(FinishedDeal(hashMapOf(
                player to setOf(GetMilk(target)),
                target to setOf(BakeCookies())
            )))
        }

        return retval
    }

    fun dealValueToMe(deal: Deal): Double{
        return DealCase(deal).dealValue(lastCasesOfConcern!!, listOf(player))[player]!!
    }

    fun justifyDeal(deal: Deal, subject: GameCharacter): List<Effect>{
        return DealCase(deal).effectsOfDeal(lastCasesOfConcern!!)
    }

    private fun mostSignificantPlayersToMe(game: Game): List<GameCharacter>{
        var retval = mutableListOf<GameCharacter>()
        retval.add(game.matchingPlayer(player)!!)//I always care about me
        game.players.forEach{
            if(!retval.contains(it)){ //TODO: make this not stupid
                retval.add(it)
            }
        }

        return retval
    }

    private fun actionPossibilitiesForPlayer(game: Game, player: GameCharacter): List<Plan>{
        var planWeights = HashMap<Action,Double>()
        val rawActions = player.actionsReguarding(mostSignificantPlayersToMe(game))

        rawActions.forEach {
            action ->
             val weight = this.player.memory.lines.fold(1.0, {acc, mem -> acc + oddsModifierGivenLine(player, action, mem)})
             planWeights.put(action, weight)
        }

        val totalWeight = planWeights.values.sum()

        return planWeights.map { Plan(player, listOf(it.key), it.value/totalWeight) }
    }

    private fun oddsModifierGivenLine(character: GameCharacter, action: Action, lineMemory: LineMemory): Double{
        if(lineMemory.line is Announcement && lineMemory.line.action!!.equals(action)){
            return 1.0
        }
        return 0.0
    }

}