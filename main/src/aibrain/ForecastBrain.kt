package aibrain

import game.action.actionTypes.BakeCookies
import game.action.actionTypes.GetPlate
import game.action.Action
import game.action.Effect
import game.Game
import game.Character
import shortstate.dialog.Memory
import shortstate.dialog.linetypes.Announcement
import util.safeSublist

class ForecastBrain {
    val player: Character

    var maxPlayersToThinkAbout = 3
    var maxPlansToThinkAbout = 9

    var lastCasesOfConcern: Map<GameCase, Double>? = null
    var lastFavoriteEffects: List<Effect>? = null
    var lastActionsToCommitTo: List<Action>? = null
    var sortedCases: List<GameCase>? = null

    constructor(player: Character){
        this.player = player
    }

    fun thinkAboutNextTurn(game: Game){
        lastCasesOfConcern = casesOfConcern(game)
        lastFavoriteEffects = favoriteEffects()
        sortedCases = lastCasesOfConcern!!.toList().sortedBy { entry -> -entry.second }.map { entry -> entry.first }
        lastActionsToCommitTo = actionsToDo(game)
        if(this.player.name == "Frip"){
            println("${player.name} thinks about $lastCasesOfConcern")
            println("${player.name} wants to $lastActionsToCommitTo")
        }
    }

    private fun favoriteEffects(): List<Effect>{
        var cases = lastCasesOfConcern

        val averageScore = cases!!.values.average()

        //map cases to how different they are from average
        val modCases = HashMap<GameCase, Double>()
        cases.forEach { entry -> modCases[entry.key] = (entry.value - averageScore) * entry.key.probability() }

        var orderedCaseList = modCases.toList().sortedBy { (key, value) -> value }

        val bestCase = orderedCaseList[orderedCaseList.size-1].first
        var bestCaseEffects = bestCase.effects.toMutableList()

        //in very primitive logic, if any case is below average, assume all effects are bad and remove them from the best case
        orderedCaseList.forEach{
            if(it.second < 0){
                //bestCaseEffects.removeAll(it.first.effects)
            }
        }

        return bestCaseEffects
    }

    private fun actionsToDo(game: Game): List<Action> {
        return lastCasesOfConcern!!.filter { entry -> entry.key.plan.player == player }.toList().sortedBy { entry -> -entry.second }[0].first.plan.actions
    }

    private fun casesOfConcern(game: Game): Map<GameCase, Double>{
        var myGame = game.imageFor(player)

        val topPlayers = safeSublist(mostSignificantPlayersToMe(myGame),0,maxPlayersToThinkAbout)

        val likelyPlans = HashMap<Character, List<Plan>>()
        topPlayers.forEach{player ->
            likelyPlans[player] = actionPossibilitiesForPlayer(myGame, player)
        }

        var retval = HashMap<GameCase,Double>()
        likelyPlans.keys.forEach {curPlayer ->
            var effects = mutableListOf<Effect>()
            //for every player that isn't this one, add the effect of every action of every plan, layered by the probability of that plan
            for(otherPlayer in likelyPlans.keys.filter{other -> other != curPlayer}) {
                for (plan in likelyPlans[otherPlayer]!!) {
                    for (action in plan.actions) {
                        effects.addAll(
                            action.type.doAction(myGame,plan.player).map { effect -> effect.layerProbability(plan.probability) })
                    }
                }
            }

            //make a gamecase for every plan of this player with effects added for the average of what everyone else might do?
            likelyPlans[curPlayer]!!.forEach {
                val toAdd = GameCase(myGame, it, effects)
                retval[toAdd] = gameValue(toAdd.game, toAdd.game.matchingPlayer(this.player)!!)
            }
        }

        return retval
    }

    private fun mostSignificantPlayersToMe(game: Game): List<Character>{
        var retval = mutableListOf<Character>()
        game.players.forEach{
            if(true){ //TODO: make this not stupid
                retval.add(it)
            }
        }

        return retval
    }

    private fun actionPossibilitiesForPlayer(game: Game, player: Character): List<Plan>{
        if(this.player.name == "Frip" && !player.npc){
            println("hook")
        }

        var planWeights = HashMap<Action,Double>()
        val rawActions = game.possibleActionsForPlayer(player)

        rawActions.forEach {
            action ->
             val weight = this.player.memory.fold(1.0, {acc, mem -> acc + oddsModifierGivenLine(player, action, mem)})
             planWeights.put(action, weight)
        }

        val totalWeight = planWeights.values.sum()

        return planWeights.map { Plan(player, listOf(it.key), it.value/totalWeight) }
    }

    private fun oddsModifierGivenLine(character: Character, action: Action, memory: Memory): Double{
        if(memory.line is Announcement && memory.line.action!!.equals(action)){
            return 1.0
        }
        return 0.0
    }

    private fun gameValue(game: Game, player: Character): Double {
        var retval = 0.0
        if(game.hasPlate.contains(player)){
            retval += game.deliciousness * 1.0
        } else {
            retval += 0.0
        }
        retval += player.dummyScore
        return retval
    }
}