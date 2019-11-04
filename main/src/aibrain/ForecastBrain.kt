package aibrain

import game.action.actionTypes.BakeCookies
import game.action.actionTypes.GetPlate
import game.action.Action
import game.action.Effect
import game.Game
import game.Character
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
            likelyPlans[player] = possibleActionsForPlayer(myGame, player)
        }

        var retval = HashMap<GameCase,Double>()
        likelyPlans.keys.forEach {player ->
            var effects = mutableListOf<Effect>()
            //for every player that isn't this one, add the effect of every game.action of every plan, layered by the probability of that plan
            for(otherPlayer in likelyPlans.keys) {
                if(otherPlayer != player) {
                    for (plan in likelyPlans[otherPlayer]!!) {
                        for (action in plan.actions) {
                            effects.addAll(
                                action.type.doAction(myGame,plan.player).map { effect -> effect.layerProbability(plan.probability) })
                        }
                    }
                }
            }

            //make a gamecase for every plan of this player with effects added for the average of what everyone else might do?
            likelyPlans[player]!!.forEach {
                val toAdd = GameCase(myGame, it, effects)
                retval[toAdd] = gameValue(toAdd.game)
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

    private fun possibleActionsForPlayer(game: Game, player: Character): List<Plan>{
        var retval = ArrayList<Plan>()
        retval.add(Plan(player, listOf<Action>(), 0.5))
        if(!player.npc){
            retval.add(Plan(player,listOf(Action(BakeCookies())), 0.5))
        } else {
            retval.add(Plan(player,listOf(Action(GetPlate())),0.5))
        }
        return retval
    }

    private fun gameValue(game: Game): Double {
        return if(game.hasPlate.contains(player)){
            game.deliciousness * 1.0
        } else {
            0.0
        }
    }
}