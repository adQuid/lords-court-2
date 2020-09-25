package gamelogic.territory.mapobjects

class GameUnit {

    val owner: Int

    constructor(owner: Int){
        this.owner = owner
    }

    constructor(other: GameUnit){
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