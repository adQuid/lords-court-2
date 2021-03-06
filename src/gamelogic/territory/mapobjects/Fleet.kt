package gamelogic.territory.mapobjects

import shortstate.ShortStateCharacter
import shortstate.ShortStateGame

class Fleet: GameUnit {

    val owner: Int
    val ships: MutableCollection<Ship>

    constructor(owner: Int){
        this.owner = owner
        this.ships = mutableSetOf()
    }

    constructor(owner: Int, ships: Collection<Ship>){
        this.owner = owner
        this.ships = ships.toMutableSet()
    }

    constructor(other: Fleet){
        owner = other.owner
        this.ships = HashSet(other.ships)
    }

    constructor(saveString: Map<String, Any>){
        owner = saveString["owner"] as Int
        ships = (saveString["ships"] as List<Map<String, Any>>).map { Ship(it) }.toMutableSet()
    }

    fun saveString(): Map<String, Any>{
        return mapOf(
            "owner" to owner,
            "ships" to ships.map { it.saveString() }
        )
    }

    override fun prettyPrint(context: ShortStateGame, perspective: ShortStateCharacter): String {
        val ownerAsCharacter = context.game.characterById(owner)

        if(perspective.player == ownerAsCharacter){
            return "your fleet"
        } else {
            return "${ownerAsCharacter.name}'s fleet"
        }
    }
}