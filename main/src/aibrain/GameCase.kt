package aibrain

import action.Effect
import game.Game

class GameCase {
    val game: Game
    val plan: Plan

    constructor(game: Game, plan: Plan, effects: List<Effect>){
        this.game = Game(game)
        this.plan = plan
        effects.forEach { it.apply(this.game) }
        this.game.commitActionsForPlayer(plan.player, plan.actions)
        this.game.endTurn()
    }

    override fun toString(): String {
        return plan.actions.fold("ACTIONS: ", {acc, action -> "$acc $action" })
    }
}