package aibrain

import actionTypes.BakeCookies
import action.Action
import action.Effect
import game.Game
import game.Player
import util.safeSublist

class Brain {
    var player: Player

    var maxPlayersToThinkAbout = 3
    var maxPlansToThinkAbout = 9

    var lastIdea: Map<GameCase, Double>? = null

    constructor(player: Player){
        this.player = player
    }

    fun casesOfConcern(game: Game): Map<GameCase, Double>{
        var myGame = game.imageFor(player)

        val topPlayers = safeSublist(mostSignificantPlayersToMe(myGame),0,maxPlayersToThinkAbout)

        val likelyPlans = HashMap<Player, List<Plan>>()
        topPlayers.forEach{player ->
            likelyPlans[player] = possibleActionsForPlayer(myGame, player)
        }

        var cases = mutableListOf<GameCase>()
        var retval = HashMap<GameCase,Double>()
        for(player in likelyPlans.keys){
            var effects = mutableListOf<Effect>()
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

            likelyPlans[player]!!.forEach {
                val toAdd = GameCase(myGame, it, effects)
                retval[toAdd] = evaluateGame(toAdd.game) * it.probability
            }
        }

        cases.forEach{
            retval.put(it, evaluateGame(it.game))
        }

        return retval
    }

    fun mostSignificantPlayersToMe(game: Game): List<Player>{
        var retval = mutableListOf<Player>()
        game.players.forEach{
            if(!it.equals(player)){
                retval.add(it)
            }
        }

        return retval
    }

    private fun possibleActionsForPlayer(game: Game, player: Player): List<Plan>{
        var retval = ArrayList<Plan>()
        retval.add(Plan(player, listOf<Action>(), 0.5))
        if(player.name == "player"){
            retval.add(Plan(player,listOf(Action(BakeCookies())), 0.5))
        }
        return retval
    }

    private fun evaluateGame(game: Game): Double {
        return game.deliciousness * 1.0
    }
}