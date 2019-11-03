package game

import aibrain.ForecastBrain
import shortstate.dialog.Memory

class Character {
    var npc: Boolean
    var brain = ForecastBrain(this)

    val name: String
    val pictureString: String
    var titles = mutableSetOf<Title>()

    var location: Location

    val memory: MutableList<Memory>

    constructor(name: String, picture: String, npc: Boolean, location: Location) {
        this.name = name
        this.pictureString = picture
        this.npc = npc
        this.location = location
        this.memory = mutableListOf()
    }

    constructor(other: Character){
        this.name = other.name
        this.pictureString = other.pictureString
        this.npc = other.npc
        this.location = other.location
        this.memory = other.memory.map{ memory -> Memory(memory)}.toMutableList()
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
        if(titles.isNotEmpty()){
            return name + ", " + titles.joinToString(", ")
        } else {
            return name
        }
    }
}