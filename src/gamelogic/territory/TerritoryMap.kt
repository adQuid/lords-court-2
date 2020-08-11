package gamelogic.territory

import com.beust.klaxon.Klaxon
import java.io.File

class TerritoryMap {

    companion object{
        fun fromMap(url: String): TerritoryMap{
            val klac = Klaxon()
            val loadMap = klac.parse<Map<String,Any>>(File(url +"/map.json").readText())!!
            return TerritoryMap(loadMap)
        }
    }

    val NEXT_ID_NAME = "next_ter_id"
    var nextId = 0
    val IMAGE_NAME = "img"
    val imageUrl: String
    val TERRITORIES_NAME = "ter"
    val territories: MutableList<Territory>

    constructor(imageUrl: String) {
        this.imageUrl = imageUrl
        this.territories = mutableListOf()
    }

    constructor(other: TerritoryMap){
        nextId = other.nextId
        imageUrl = other.imageUrl
        territories = other.territories.map { Territory(it) }.toMutableList()
    }

    constructor(saveString: Map<String, Any>){
        nextId = saveString[NEXT_ID_NAME] as Int
        imageUrl = saveString[IMAGE_NAME] as String
        territories = (saveString[TERRITORIES_NAME] as List<Map<String, Any>>).map{ Territory(it)}.toMutableList()
    }

    fun saveString(): Map<String, Any>{
        return mapOf(
            NEXT_ID_NAME to nextId,
            IMAGE_NAME to imageUrl,
            TERRITORIES_NAME to territories.map { it.saveString() }
        )
    }
}