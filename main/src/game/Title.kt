package game

import game.action.Action

class Title {

    val name: String
    val actionsEntitled: List<Action.ActionType>

    constructor(name: String, actions: List<Action.ActionType>){
        this.name = name
        actionsEntitled = actions
    }

}