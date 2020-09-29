package gamelogic.territory.mapobjects

class Structure {

    val owner: Int
    val type: StructureType

    constructor(owner: Int, type: StructureType){
        this.owner = owner
        this.type = type
    }

    constructor(other: Structure){
        owner = other.owner
        type = other.type
    }

    constructor(saveString: Map<String, Any>){
        owner = saveString["owner"] as Int
        type = StructureType.typeByName(saveString["type"] as String)
    }

    fun saveString(): Map<String, Any>{
        return mapOf(
            "owner" to owner,
            "type" to type.name
        )
    }

}