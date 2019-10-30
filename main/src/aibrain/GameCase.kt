package aibrain

import game.action.Effect
import game.Game

class GameCase {
    val game: Game
    val plan: Plan
    var effects = listOf<Effect>()

    constructor(game: Game, plan: Plan, effects: List<Effect>){
        this.game = Game(game)
        this.plan = plan
        effects.forEach { addEffect(it) }
        plan.actions.forEach { action -> action.type.doAction(game, plan.player).forEach { addEffect(it) } }
        this.game.commitActionsForPlayer(plan.player, plan.actions)
        this.game.endTurn()
    }

    fun addEffect(effect: Effect){
        effects = effects + effect
    }

    fun probability(): Double{
        return plan.probability
    }

    override fun toString(): String {
        return plan.actions.fold("ACTIONS BY ${plan.player.toString()}: ", {acc, action -> "$acc $action" })
    }
}