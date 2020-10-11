package gamelogic.territory.mapobjects

import com.beust.klaxon.Klaxon
import game.Game
import gamelogic.resources.Resources
import shortstate.ShortStateCharacter
import ui.Describable
import java.io.File

class StructureType: Describable {
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

    val name: String

    val cost: Resources
    val constructionThroughput: Int

    val manufactoring: Collection<ManufactorOption>

    constructor(saveString: Map<String, Any>){
        name = saveString["name"] as String
        cost = Resources(saveString["cost"] as Map<String, Any>)
        constructionThroughput = saveString["construction throughput"] as Int
        manufactoring = (saveString["manufactoring"] as List<Map<String, Any>>).map { ManufactorOption(it) }
    }

    override fun description(): String {
        return "NOT IMPLEMENTED"
    }

    override fun tooltip(perspective: ShortStateCharacter): String {
        return name
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

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as ManufactorOption

            if (thruput != other.thruput) return false
            if (inputs != other.inputs) return false
            if (outputs != other.outputs) return false

            return true
        }

        override fun hashCode(): Int {
            var result = thruput
            result = 31 * result + inputs.hashCode()
            result = 31 * result + outputs.hashCode()
            return result
        }


    }
}