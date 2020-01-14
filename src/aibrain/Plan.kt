package aibrain

import game.action.Action
import game.Character

class Plan {
    val player: Character
    val actions: List<Action>
    val probability: Double

    constructor(player: Character, actions: List<Action>, probability: Double){
        this.player = player
        this.actions = actions
        this.probability = probability
    }
}