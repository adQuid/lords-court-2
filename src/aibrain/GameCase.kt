package aibrain

import game.Effect
import game.Game

class GameCase {
    val baseGame: Game
    var currentGame: Game
    val plan: Plan
    var effects = listOf<Effect>()

    constructor(game: Game, plan: Plan, effects: List<Effect>){
        this.baseGame = Game(game)
        this.plan = plan
        effects.forEach { addEffect(it) }
        this.currentGame = calculateGame()
    }

    constructor(other: GameCase){
        this.baseGame = Game(other.baseGame)
        this.plan = other.plan
        other.effects.forEach { addEffect(it) }
        this.currentGame = calculateGame()
    }

    private fun calculateGame(): Game{
        val tempGame = Game(baseGame)
        tempGame.commitActionsForPlayer(plan.player, plan.actions)
        tempGame.endTurn()
        effects.forEach { it.apply(tempGame) }
        return tempGame
    }

    fun applyDeal(deal: Deal): GameCase{
        val retval = GameCase(this)
        deal.theActions().keys.forEach {
            retval.baseGame.applyActions(deal.theActions()[it]!!, it)
        }
        retval.currentGame = retval.calculateGame()
        return retval
    }

    fun addEffect(effect: Effect){
        effects = effects + effect
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

    private fun gameValue(game: Game, player: game.GameCharacter): Double {
        var retval = 0.0
        if(game.hasMilk.contains(player)){
            retval += game.deliciousness * 1.0
        } else {
            retval += 0.0
        }
        retval += player.dummyScore
        return retval
    }
}