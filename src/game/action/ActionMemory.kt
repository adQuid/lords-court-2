package game.action

import game.Game

class ActionMemory {
    val ACTION_NAME = "ACTION"
    val action: Action
    val AGE_NAME = "AGE"
    var age: Int

    constructor(action: Action){
        this.action = action
        age = 0
    }

    constructor(other: ActionMemory){
        action = other.action
        age = other.age
    }

    constructor(saveString: Map<String, Any>, game: Game){
        age = saveString[AGE_NAME] as Int
        action = GlobalActionTypeFactory.fromMap(saveString[ACTION_NAME] as Map<String, Any>, game)
    }

    fun saveString(): Map<String, Any>{
        return hashMapOf(
            AGE_NAME to age,
            ACTION_NAME to action.saveString()
        )
    }
}