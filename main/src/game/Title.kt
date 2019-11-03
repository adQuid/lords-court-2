package game

import game.action.Action

class Title {

    val name: String
    val actionsEntitled: List<Action.ActionType>

    constructor(name: String, actions: List<Action.ActionType>){
        this.name = name
        actionsEntitled = actions
    }

    constructor(other: Title){
        this.name = other.name
        actionsEntitled = other.actionsEntitled.map { type -> type }
    }

    override fun toString(): String {
        return name
    }
}