package aibrain

import game.action.Action
import game.Effect
import game.Game
import game.GameCharacter
import game.action.actionTypes.BakeCookies
import game.action.actionTypes.GetMilk
import game.action.actionTypes.WasteTime
import shortstate.dialog.Memory
import shortstate.dialog.linetypes.Announcement
import util.safeSublist

class ForecastBrain {
    val player: GameCharacter

    var maxPlayersToThinkAbout = 3

    //TODO: Why do both of these exist?
    var lastCasesOfConcern: List<GameCase>? = null
    var dealsILike: List<FinishedDeal>? = null

    var lastFavoriteEffects: List<Effect>? = null
    var lastActionsToCommitTo: List<Action>? = null


    constructor(player: GameCharacter){
        this.player = player
    }

    fun thinkAboutNextTurn(game: Game){
        lastCasesOfConcern = casesOfConcern(game).toList().sortedBy { case -> -case.valueToCharacter(player) }
        lastFavoriteEffects = favoriteEffects()
        lastActionsToCommitTo = actionsToDo(game)

        //DEBUG
        if(this.player.name == "Frip"){
            println("${player.name} thinks about $lastCasesOfConcern")
            println("${player.name} wants to $lastActionsToCommitTo")
        }

        dealsILike = dealsILike(game)
    }

    private fun favoriteEffects(): List<Effect>{
        var cases = lastCasesOfConcern

        val averageScore = cases!!.map{case -> case.valueToCharacter(player)}.average()

        //map cases to how different they are from average
        val modCases = HashMap<GameCase, Double>()
        cases.forEach { case -> modCases[case] = (case.valueToCharacter(player) - averageScore) * case.probability() }

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

    private fun dealsILike(game: Game): List<FinishedDeal> {
        return mostSignificantPlayersToMe(game).filter { it -> it != player }
            .map { character -> prospectiveDealsWithPlayer(character) }.flatten()
            .filter { dealValue(it) > 0 }
    }

    private fun actionsToDo(game: Game): List<Action> {
        return lastCasesOfConcern!!.filter { case -> case.plan.player == player }.toList().sortedBy { case -> -case.valueToCharacter(player) }[0].plan.actions
    }

    private fun casesOfConcern(game: Game): List<GameCase>{
        var myGame = game.imageFor(player)

        val topPlayers = safeSublist(mostSignificantPlayersToMe(myGame),0,maxPlayersToThinkAbout)

        val likelyPlans = HashMap<GameCharacter, List<Plan>>()
        topPlayers.forEach{player ->
            likelyPlans[player] = actionPossibilitiesForPlayer(myGame, player)
        }

        var retval = mutableListOf<GameCase>()
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
                retval.add(toAdd)
            }
        }

        return retval
    }

    fun prospectiveDealsWithPlayer(target: GameCharacter): List<FinishedDeal>{
        val badDeal = FinishedDeal(hashMapOf(
            player to listOf(Action(WasteTime())),
            target to listOf(Action(WasteTime()))
        ))

        val goodDeal = FinishedDeal(hashMapOf(
            player to listOf(Action(GetMilk(target)), Action(WasteTime())),
            target to listOf(Action(BakeCookies()))
        ))

        return listOf(badDeal, goodDeal)
    }

    //returns the marginal multiple of previous value. For example, if the total of gamecase value was 100 before the deal and 120 after,
    //this would return 0.2
    fun dealValue(deal: Deal): Double{
        val casesWithDeal = lastCasesOfConcern!!.map { it.applyDeal(deal) }
        return (totalCaseValue(casesWithDeal) - totalCaseValue(lastCasesOfConcern!!)) /totalCaseValue(lastCasesOfConcern!!)
    }

    private fun totalCaseValue(reality: List<GameCase>): Double{
        return reality.fold(0.0, { acc, case -> acc + case.valueToCharacter(player) })
    }

    private fun mostSignificantPlayersToMe(game: Game): List<GameCharacter>{
        var retval = mutableListOf<GameCharacter>()
        game.players.forEach{
            if(true){ //TODO: make this not stupid
                retval.add(it)
            }
        }

        return retval
    }

    private fun actionPossibilitiesForPlayer(game: Game, player: GameCharacter): List<Plan>{
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

    private fun oddsModifierGivenLine(character: GameCharacter, action: Action, memory: Memory): Double{
        if(memory.line is Announcement && memory.line.action!!.equals(action)){
            return 1.0
        }
        return 0.0
    }

}