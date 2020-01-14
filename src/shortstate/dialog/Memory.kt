package shortstate.dialog

import shortstate.dialog.linetypes.GlobalLineTypeList

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

    constructor(map: Map<String, Any>){
        age = map["AGE"] as Int
        line = GlobalLineTypeList.fromMap(map["LINE"] as Map<String, Any>)
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