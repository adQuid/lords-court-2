package aibrain

import action.Action
import game.Player

class Plan {
    val player: Player
    val actions: List<Action>
    val probability: Double

    constructor(player: Player, actions: List<Action>, probability: Double){
        this.player = player
        this.actions = actions
        this.probability = probability
    }
}