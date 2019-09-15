package aibrain

import game.Game

class GameCase {
    val game: Game
    val probability: Double

    constructor(game: Game, potentialActions: PotentialActions){
        this.game = Game(game)
        this.game.actionsByPlayer[potentialActions.player] = potentialActions.actions
        probability = potentialActions.probability
    }

    override fun toString(): String {
        return game.actionsByPlayer.keys.fold("ACTIONS: ", {acc,
                                                              key -> acc + " for player "+ key.name + ": " + game.actionsByPlayer[key]!!.fold("", {acc2, action -> acc2 + action.type.toString()})})
    }
}