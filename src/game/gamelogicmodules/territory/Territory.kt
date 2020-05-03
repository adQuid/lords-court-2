package game.gamelogicmodules.territory

import game.Title
import game.gamelogicmodules.resources.Resources

class Territory {

    companion object{
        var nextId = 0

        val ARABLE_LAND_NAME = "arable_land"
        val POPULATION_NAME = "pop"
        val SEEDS_NAME = "seeds"
        val FLOUR_NAME = "flour"
        val BREAD_NAME = "bread"
        val RESOURCES_NAME = "resources"
        private val resourceNames = listOf(ARABLE_LAND_NAME, POPULATION_NAME, SEEDS_NAME, FLOUR_NAME, BREAD_NAME, RESOURCES_NAME)
    }

    val ID_NAME = "ID"
    val id: Int
    val NAME_NAME = "name"
    val name: String

    val CROPS_NAME = "crops"
    val crops: MutableList<Crop>
    val resources: Resources
    constructor(name: String){
        id = nextId++
        this.name = name
        resources = Resources()
        resources.set(ARABLE_LAND_NAME, 100)
        resources.set(POPULATION_NAME, 100)
        resources.set(SEEDS_NAME, 100)
        crops = mutableListOf()
    }

    constructor(other: Territory){
        id = other.id
        name = other.name
        resources = Resources(other.resources)
        crops = other.crops.map { Crop(it) }.toMutableList()
    }

    constructor(saveString: Map<String, Any>){
        id = saveString[ID_NAME] as Int
        name = saveString[NAME_NAME] as String
        resources = Resources(saveString[RESOURCES_NAME] as MutableMap<String, Any>)
        crops = (saveString[CROPS_NAME] as List<Map<String, Any>>).map{map -> Crop(map)}.toMutableList()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Territory

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }

    fun saveString(): Map<String, Any>{
        return mapOf(
            ID_NAME to id,
            NAME_NAME to name,
            RESOURCES_NAME to resources.saveString(),
            CROPS_NAME to crops.map { it.saveString() }
        )
    }

    fun generateCountTitle(): Title{
        return Count(this)
    }

    fun modifyResource(resource: String, value: Int){
        resources.add(resource, value)
    }

    fun totalCropsPlanted(): Int{
        return crops.sumBy { it.quantity }
    }
}