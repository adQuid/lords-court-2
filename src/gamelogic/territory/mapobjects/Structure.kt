package gamelogic.territory.mapobjects

import game.Game
import game.GameCharacter

class Structure {

    val owner: Int

    constructor(owner: Int){
        this.owner = owner
    }

    constructor(other: Structure){
        owner = other.owner
    }

    constructor(saveString: Map<String, Any>){
        owner = saveString["owner"] as Int
    }

    fun saveString(): Map<String, Any>{
        return mapOf(
            "owner" to owner
        )
    }

}