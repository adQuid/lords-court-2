package aibrain

import game.Action
import game.Player

class Plan {
    val actions: List<Map<Player, Action>>

    constructor(actions: List<Map<Player, Action>>){
        this.actions = actions
    }
}