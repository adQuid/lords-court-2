package game

import aibrain.Brain

class Player {
    val name: String
    var npc: Boolean
    var brain = Brain(this)

    constructor(name: String, npc: Boolean) {
        this.name = name
        this.npc = npc
    }

    constructor(other: Player){
        this.name = other.name
        this.npc = other.npc
    }

    override fun equals(other: Any?): Boolean {
        if(other is Player){
            return this.name == other.name
        }
        return false
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    override fun toString(): String{
        return name
    }
}