package gamelogic.territory.mapobjects

import gamelogic.resources.Resources
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
        val manufactureOption = bestManufactureOption(inputTypes, outputType)

        if(manufactureOption == null){
            return mapOf()
        } else {
            return manufactureOption!!.outputs
        }
    }

    fun bestManufactureOption(inputTypes: Collection<String>, outputType: String): StructureType.ManufactorOption? {

        val retval = if(manufatureTypeSelected != null) {
            if(expended()){
                 return null
            } else{
                manufatureTypeSelected
            }
        } else {
            type.manufactoring.filter { inputTypes.containsAll(it.inputs.keys) }.sortedByDescending { it.outputs[outputType] }.firstOrNull()
        }

        return retval
    }

    fun runManufacture(manufacture: StructureType.ManufactorOption, quantity: Int): Resources {
        if(!type.manufactoring.contains(manufacture)){
            throw Exception("Attempted to manufacture illegal type from building of type ${type.name}!")
        }

        if(manufatureTypeSelected != null && manufatureTypeSelected != manufacture){
            throw Exception("Attempted to change manufacture type after starting!")
        }

        manufatureTypeSelected = manufacture

        if(usesExpended + quantity > manufatureTypeSelected!!.thruput){
            throw Exception("Overused manufacturing type!")
        }

        usesExpended += quantity

        val retval = Resources(manufatureTypeSelected!!.outputs)
        retval.subtractAll(Resources(manufatureTypeSelected!!.inputs))

        return retval
    }

    fun expended(): Boolean{
        return manufatureTypeSelected != null && usesExpended == manufatureTypeSelected!!.thruput
    }

}