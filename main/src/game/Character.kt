package game

import aibrain.ForecastBrain

class Character {
    var npc: Boolean
    var brain = ForecastBrain(this)

    val name: String
    val pictureString: String
    var titles = mutableSetOf<Title>()

    var location: Location

    constructor(name: String, picture: String, npc: Boolean, location: Location) {
        this.name = name
        this.pictureString = picture
        this.npc = npc
        this.location = location
    }

    constructor(other: Character){
        this.name = other.name
        this.pictureString = other.pictureString
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
        if(titles.isNotEmpty()){
            return name + ", " + titles.joinToString(", ")
        } else {
            return name
        }
    }
}