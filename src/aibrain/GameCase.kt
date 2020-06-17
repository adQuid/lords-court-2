package aibrain

import game.Effect
import game.Game
import game.GameCharacter

class GameCase {
    companion object{
        val LOOKAHEAD = 120
    }

    val baseGame: Game
    var currentGame: Game
    val plan: Plan
    //effects imposed on the inside from outside the simulation
    var initialEffects = listOf<Effect>()
    //all effects that applied to this game after simulation
    var finalEffects = mutableListOf<Effect>()

    constructor(game: Game, player: GameCharacter){
        this.baseGame = game
        this.plan = Plan(player, listOf(), 1.0)
        this.currentGame = calculateGame()
    }

    constructor(game: Game, plan: Plan, effects: List<Effect>){
        this.baseGame = Game(game)
        this.plan = plan
        effects.forEach { addEffect(it) }
        this.currentGame = calculateGame()
    }

    constructor(other: GameCase){
        this.baseGame = Game(other.baseGame)
        this.plan = other.plan
        other.initialEffects.forEach { addEffect(it) }
        this.currentGame = calculateGame()
    }

    private fun calculateGame(): Game{
        val tempGame = Game(baseGame)
        initialEffects.forEach { it.apply(tempGame) }
        if(plan.player != null){
            tempGame.appendActionsForPlayer(plan.player, plan.actions)
        }
        finalEffects.addAll(initialEffects)

        for ( turn in 1..LOOKAHEAD){
            //this has the side effect of actually ending the turn
            finalEffects.addAll(listOf(initialEffects, tempGame.endTurn()).flatten().toMutableList())
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

    fun addEffect(effect: Effect){
        initialEffects = initialEffects + effect
    }

    fun probability(): Double{
        return plan.probability
    }

    override fun toString(): String {
        return plan.actions.fold("{If  ${plan.player.toString()} did: ", {acc, action -> "$acc $action" }) + "}"
    }

    fun valueToCharacter(character: game.GameCharacter): Double{
        return gameValue(this.currentGame, this.currentGame.matchingPlayer(character)!!)
    }

    private fun gameValue(game: Game, player: GameCharacter): Double {
        var retval = 0.0
        game.gameLogicModules.forEach {
            retval += it.value(player)
        }
        retval += player.dummyScore
        return retval
    }
}