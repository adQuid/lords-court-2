package aibrain

import game.action.Action
import game.Character

class Deal {

    val actions: Map<Character, List<Action>>

    constructor(actions: Map<Character, List<Action>>){
        this.actions = actions
    }

}