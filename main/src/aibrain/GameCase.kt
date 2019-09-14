package aibrain

import game.Game

class GameCase {
    val game: Game
    val probability: Double

    constructor(game: Game, potentialActions: PotentialActions){
        this.game = game
        this.game.actionsByPlayer[potentialActions.player] = potentialActions.actions
        probability = potentialActions.probability
    }

}