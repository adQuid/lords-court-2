package shortstate.dialog

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

    fun age(){
        age++
    }
}