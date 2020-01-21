package shortstate.dialog

import game.Game

class Memory {
    var age = 0
    val line: Line

    constructor(line: Line){
        this.line = line
    }

    constructor(other: Memory){
        this.age = other.age
        this.line = other.line
    }

    constructor(map: Map<String, Any>, parent: Game){
        age = map["AGE"] as Int
        line = GlobalLineTypeFactory.fromMap(map["LINE"] as Map<String, Any>, parent)
    }

    fun saveString(): Map<String, Any>{
        return hashMapOf(
            "AGE" to age,
            "LINE" to line.saveString()
        )
    }

    fun age(){
        age++
    }
}