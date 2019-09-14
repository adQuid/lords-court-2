package game

class Player {
    val name: String

    constructor(name: String) {
        this.name = name
    }

    constructor(other: Player){
        this.name = other.name
    }

    override fun equals(other: Any?): Boolean {
        if(other is Player){
            return this.name == other.name
        }
        return false
    }
}