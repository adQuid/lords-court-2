package gamelogic.territory.mapobjects

import com.beust.klaxon.Klaxon
import java.io.File

class ShipType {
    companion object{
        var allTypes = listOf<ShipType>()

        fun loadShipTypes(){
            val klac = Klaxon()
            val loadMap = klac.parseArray<Map<String,Any>>(File("data/ship_types.txt").readText())!!
            allTypes = loadMap.map { ShipType(it) }
        }

        fun typeByName(name: String): ShipType{
            if(allTypes.isEmpty()){
                loadShipTypes()
            }
            return allTypes.first { it.name == name }
        }
    }

    val name: String

    val resourceExtractions: Map<String, Int>

    constructor(saveString: Map<String, Any>){
        name = saveString["name"] as String
        resourceExtractions = saveString["resource extractions"] as Map<String, Int>
    }

}