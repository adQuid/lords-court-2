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
    }

    val name: String

    constructor(saveString: Map<String, Any>){
        name = saveString["name"] as String
    }

}