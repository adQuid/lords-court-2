package game

import aibrain.ConversationBrain
import aibrain.ForecastBrain

class Player {
    var npc: Boolean
    var brain = ForecastBrain(this)
    var shortBrain = ConversationBrain(brain)

    val name: String
    var location: Location

    constructor(name: String, npc: Boolean, location: Location) {
        this.name = name
        this.npc = npc
        this.location = location
    }

    constructor(other: Player){
        this.name = other.name
        this.npc = other.npc
        this.location = other.location
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