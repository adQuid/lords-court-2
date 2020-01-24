package aibrain

import game.action.Action
import game.GameCharacter

class Plan {
    val player: GameCharacter
    val actions: List<Action>
    val probability: Double

    constructor(player: GameCharacter, actions: List<Action>, probability: Double){
        this.player = player
        this.actions = actions
        this.probability = probability
    }
}