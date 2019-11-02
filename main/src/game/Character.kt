package game

import aibrain.ForecastBrain

class Character {
    var npc: Boolean
    var brain = ForecastBrain(this)

    val name: String
    var titles = mutableSetOf<Title>()

    var location: Location

    constructor(name: String, npc: Boolean, location: Location) {
        this.name = name
        this.npc = npc
        this.location = location
    }

    constructor(other: Character){
        this.name = other.name
        this.npc = other.npc
        this.location = other.location
    }

    override fun equals(other: Any?): Boolean {
        if(other is Character){
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

    fun fullName(): String{
        return name + ", " + titles.joinToString(", ")
    }
}