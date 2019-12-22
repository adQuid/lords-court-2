package aibrain

import game.action.Effect
import game.Game

class GameCase {
    val baseGame: Game
    val currentGame: Game
    val plan: Plan
    var effects = listOf<Effect>()

    constructor(game: Game, plan: Plan, effects: List<Effect>){
        this.baseGame = Game(game)
        this.plan = plan
        effects.forEach { addEffect(it) }
        this.currentGame = calculateGame()
    }

    private fun calculateGame(): Game{
        val tempGame = Game(baseGame)
        tempGame.commitActionsForPlayer(plan.player, plan.actions)
        tempGame.endTurn()
        effects.forEach { it.apply(tempGame) }
        return tempGame
    }

    fun applyDeal(deal: Deal){
        deal.actions.keys.forEach {
            baseGame.applyActions(deal.actions[it]!!, it)
        }
        calculateGame()
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

    fun valueToCharacter(character: game.Character): Double{
        return gameValue(this.currentGame, character)
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