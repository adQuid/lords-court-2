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
        effect.apply(game)
    }

    fun probability(): Double{
        return plan.probability
    }

    override fun toString(): String {
        return plan.actions.fold("{If  ${plan.player.toString()} did: ", {acc, action -> "$acc $action" }) + "}"
    }

    fun valueToCharacter(character: game.Character): Double{
        return gameValue(this.game, character)
    }

    private fun gameValue(game: Game, player: game.Character): Double {
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