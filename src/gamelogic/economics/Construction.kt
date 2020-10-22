package gamelogic.economics

import gamelogic.resources.Resources
import gamelogic.territory.mapobjects.Structure

class Construction {
    
    val structure: Structure
    var budget: Resources
    val spentResources: Resources

    constructor(structure: Structure){
        this.structure = structure
        this.budget = Resources()
        this.spentResources = Resources()
    }

    constructor(structure: Structure, budget: Resources){
        this.structure = structure
        this.budget = budget
        this.spentResources = Resources()
    }

    constructor(other: Construction){
        this.structure = Structure(other.structure) //Structures are immutable, right?
        this.budget = Resources(other.budget)
        this.spentResources = Resources(other.spentResources)
    }

    constructor(saveString: Map<String, Any>){
        structure = Structure(saveString["structure"] as Map<String, Any>)
        budget = Resources(saveString["budget"] as Map<String, Any>)
        spentResources = Resources(saveString["spent"] as Map<String, Any>)
    }

    fun saveString(): Map<String, Any>{
        return mapOf(
            "structure" to structure.saveString(),
            "budget" to budget.saveString(),
            "spent" to spentResources.saveString()
        )
    }

    fun resourceNeeded(name: String): Int{
        return structure.type.cost.get(name) - spentResources.get(name)
    }

    fun complete(): Boolean{
        return structure.type.cost == spentResources
    }
}