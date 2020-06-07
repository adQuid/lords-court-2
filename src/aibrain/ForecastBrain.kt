package aibrain

import game.action.Action
import game.Effect
import game.Game
import game.GameCharacter
import gamelogicmodules.cookieworld.actionTypes.BakeCookies
import gamelogicmodules.cookieworld.actionTypes.GetMilk
import gamelogicmodules.cookieworld.actionTypes.WasteTime
import gamelogicmodules.cookieworld.CookieWorld
import main.Controller
import shortstate.dialog.LineMemory
import shortstate.dialog.linetypes.Announcement
import util.safeSublist

class ForecastBrain {
    val player: GameCharacter

    var lastGameIEvaluated: Game? = null

    var maxPlayersToThinkAbout = 3

    //things that other important people might do
    var lastCasesOfConcern: List<GameCase>? = null
    //things I could do
    var dealsILike: Map<FinishedDeal, Double>? = null
        get() {if(field == null){thinkAboutNextTurn(Controller.singleton!!.game!!)}; return field} //I presume this call is still safe because the brain only runs on the real game

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
        dealsILike = dealsILike(myGame)
        if(dealsILike!!.filter { it.key.actions.keys.size == 1 }.isNotEmpty()){
            lastActionsToCommitTo = dealsILike!!.filter { it.key.actions.keys.size == 1 }.keys.toList().sortedBy { dealValueToMe(it) }.first().actions[player]!!.toList() //TODO: cry
        } else {
            lastActionsToCommitTo = listOf()
        }

        //DEBUG
        if(this.player.name == "Frip"){
            println("${player.name} thinks about $lastCasesOfConcern")
            println("${player.name} wants to $lastActionsToCommitTo")
        }

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
        val debug = mostSignificantPlayersToMe(game)
            .map { character -> prospectiveDealsWithPlayer(game, character) }.flatten().associateWith { deal -> dealValueToMe(deal) }
        return mostSignificantPlayersToMe(game)
            .map { character -> prospectiveDealsWithPlayer(game, character) }.flatten()
            .filter { dealValueToMe(it) > 0 }.associateWith { deal -> dealValueToMe(deal) }
    }

    private fun casesOfConcern(game: Game): List<GameCase>{
        val topPlayers = safeSublist(mostSignificantPlayersToMe(game),0,maxPlayersToThinkAbout)

        val likelyPlans = HashMap<GameCharacter, List<Plan>>()
        topPlayers.forEach{player ->
            if(player != this.player){
                likelyPlans[player] = actionPossibilitiesForPlayer(game, player)
            }
        }

        //special case where there are no other players who can act
        if(likelyPlans.values.isEmpty()){
            return listOf(GameCase(game, player))
        }
        return likelyPlans.values.flatten().map{GameCase(game, it, listOf())}
    }

    private fun prospectiveDealsWithPlayer(game: Game, target: GameCharacter): List<FinishedDeal>{

        if(target == player){
            return actionPossibilitiesForPlayer(game, target).map{ FinishedDeal(mapOf(target to it.actions.toSet())) }
        }

        val retval = mutableListOf<FinishedDeal>()

        if(game.gameLogicModules.filter { it is CookieWorld }.isNotEmpty()){
            retval.add(FinishedDeal(hashMapOf(
                player to setOf(WasteTime()),
                target to setOf(WasteTime())
            )))

            retval.add(FinishedDeal(hashMapOf(
                player to setOf(GetMilk(player), GetMilk(target)),
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

    private fun actionPossibilitiesForPlayer(game: Game, target: GameCharacter): List<Plan>{

        //TODO: reimplement this
        /*rawActions.forEach {
            action ->
             val weight = this.target.memory.lines.fold(1.0, {acc, mem -> acc + oddsModifierGivenLine(target, action, mem)})
             planWeights.put(action, weight)
        }*/

        val debug = game.gameLogicModules.flatMap { it.planOptions(player, listOf(target)) }
        return game.gameLogicModules.flatMap { it.planOptions(player, listOf(target)) }
    }

    private fun oddsModifierGivenLine(character: GameCharacter, action: Action, lineMemory: LineMemory): Double{
        if(lineMemory.line is Announcement && lineMemory.line.action!!.equals(action)){
            return 1.0
        }
        return 0.0
    }

}