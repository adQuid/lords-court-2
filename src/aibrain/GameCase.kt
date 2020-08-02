package aibrain

import game.Game
import game.GameCharacter

class GameCase {
    companion object{
        val LOOKAHEAD = 120
    }

    val baseGame: Game
    var currentGame: Game
    val plan: Plan

    constructor(game: Game, player: GameCharacter){
        this.baseGame = game
        this.plan = Plan(player, listOf(), 1.0)
        this.currentGame = calculateGame()
    }

    constructor(game: Game, plan: Plan){
        this.baseGame = Game(game)
        this.plan = plan
        this.currentGame = calculateGame()
    }

    constructor(other: GameCase){
        this.baseGame = Game(other.baseGame)
        this.plan = other.plan
        this.currentGame = calculateGame()
    }

    private fun calculateGame(): Game{
        val tempGame = Game(baseGame)
        if(plan.player != null){
            tempGame.appendActionsForPlayer(plan.player, plan.actions)
        }

        for ( turn in 1..LOOKAHEAD){
            tempGame.endTurn()
        }
        return tempGame
    }

    fun applyDeal(deal: Deal): GameCase{
        val retval = GameCase(this)
        deal.theActions().forEach {
            retval.baseGame.appendActionsForPlayer(it.key, it.value.toList())
        }
        /*deal.theActions().keys.forEach {
            retval.finalEffects.addAll(retval.baseGame.applyActions(deal.theActions()[it]!!, it))
        }*/
        retval.currentGame = retval.calculateGame()
        return retval
    }

    fun probability(): Double{
        return plan.probability
    }

    override fun toString(): String {
        return plan.actions.fold("{If  ${plan.player.toString()} did: ", {acc, action -> "$acc $action" }) + "}"
    }

    fun valueToCharacter(character: GameCharacter): Double{
        return gameScore(this.currentGame.matchingPlayer(character)!!).components().sumByDouble { it.value }
    }

    fun gameScore(player: GameCharacter): Score {
        return Score(this.currentGame.gameLogicModules.flatMap {
            it.score(player).components()
        })
    }
}