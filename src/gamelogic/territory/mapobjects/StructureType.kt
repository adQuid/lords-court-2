package gamelogic.territory.mapobjects

import com.beust.klaxon.Klaxon
import game.Game
import java.io.File

class StructureType {
    companion object{
        var allTypes = listOf<StructureType>()

        fun loadStructureTypes(){
            val klac = Klaxon()
            val loadMap = klac.parseArray<Map<String,Any>>(File("data/structure_types.txt").readText())!!
            allTypes = loadMap.map { StructureType(it) }
        }

        fun typeByName(name: String): StructureType{
            if(allTypes.isEmpty()){
                loadStructureTypes()
            }
            return allTypes.first { it.name == name }
        }
    }

    class ManufactorOption{
        val thruput: Int
        val inputs: Map<String, Int>
        val outputs: Map<String, Int>

        constructor(saveString: Map<String, Any>) {
            this.thruput = saveString["thruput"] as Int
            this.inputs = saveString["input"] as Map<String, Int>
            this.outputs = saveString["output"] as Map<String, Int>
        }
    }

    val name: String

    val manufactoring: Collection<ManufactorOption>

    constructor(saveString: Map<String, Any>){
        name = saveString["name"] as String

        manufactoring = (saveString["manufactoring"] as List<Map<String, Any>>).map { ManufactorOption(it) }
    }
}