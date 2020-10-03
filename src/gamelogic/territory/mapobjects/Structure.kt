package gamelogic.territory.mapobjects

import kotlin.math.exp

class Structure {

    val owner: Int
    val type: StructureType

    //these do not need to be serialized, since these should be only used by the end turn calculation of EconomicsLogicModule
    var manufatureTypeSelected: StructureType.ManufactorOption? = null
    var usesExpended = 0

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

    fun bestConversion(inputTypes: Collection<String>, outputType: String): Map<String, Int>{

        val retval = if(manufatureTypeSelected != null) {
            if(expended()){
                 return mapOf()
            } else{
                manufatureTypeSelected
            }
        } else {
            type.manufactoring.filter { inputTypes.containsAll(it.inputs.keys) }.sortedByDescending { it.outputs[outputType] }.firstOrNull()
        }

        if(retval != null){
            return retval!!.outputs
        } else {
            return mapOf()
        }
    }

    fun expended(): Boolean{
        return manufatureTypeSelected != null && usesExpended == manufatureTypeSelected!!.thruput
    }

}