package game

class Action {
    val type: ActionType

    constructor(type: ActionType){
        this.type = type
    }

    abstract class ActionType{
        abstract fun doAction(game: Game, player: Player)
    }
}