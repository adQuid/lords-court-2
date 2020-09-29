package gamelogic.territory.mapobjects

class Ship {

    val type: ShipType

    constructor(type: ShipType){
        this.type = type
    }

    constructor(saveString: Map<String, Any>){
        type = ShipType.typeByName(saveString["type"] as String)
    }

    fun saveString(): Map<String, Any>{
        return mapOf(
            "type" to type.name
        )
    }

}