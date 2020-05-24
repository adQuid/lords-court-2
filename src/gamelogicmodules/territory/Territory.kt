package gamelogicmodules.territory

import gamelogicmodules.resources.Resources

class Territory {

    companion object{
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
    var name: String
    val x: Int
    val y: Int


    val CROPS_NAME = "crops"
    val crops: MutableList<Crop>
    val LAST_HARVEST_NAME = "lastharvest"
    var lastHarvest: List<Crop>
    val resources: Resources
    constructor(id: Int, name: String, x: Int, y: Int){
        this.id = id
        this.name = name
        this.x = x
        this.y = y
        resources = Resources()
        resources.set(ARABLE_LAND_NAME, 200)
        resources.set(POPULATION_NAME, 100)
        resources.set(SEEDS_NAME, 100)
        resources.set(FLOUR_NAME, 1000)
        resources.set(BREAD_NAME, 100)
        crops = mutableListOf()
        lastHarvest = listOf()
    }

    constructor(other: Territory){
        id = other.id
        name = other.name
        x = other.x
        y = other.y
        resources = Resources(other.resources)
        crops = other.crops.map { Crop(it) }.toMutableList()
        lastHarvest = other.lastHarvest //don't need to deep copy since it's not mutable
    }

    constructor(saveString: Map<String, Any>){
        id = saveString[ID_NAME] as Int
        name = saveString[NAME_NAME] as String
        x = saveString["x"] as Int
        y = saveString["y"] as Int
        resources = Resources(saveString[RESOURCES_NAME] as MutableMap<String, Any>)
        crops = (saveString[CROPS_NAME] as List<Map<String, Any>>).map{map -> Crop(map)}.toMutableList()
        lastHarvest = (saveString[LAST_HARVEST_NAME] as List<Map<String,Any>>).map{map -> Crop(map)}
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
            "x" to x,
            "y" to y,
            RESOURCES_NAME to resources.saveString(),
            CROPS_NAME to crops.map { it.saveString() },
            LAST_HARVEST_NAME to lastHarvest.map{ it.saveString() }
        )
    }

    fun modifyResource(resource: String, value: Int){
        resources.add(resource, value)
    }

    fun totalCropsPlanted(): Int{
        return crops.sumBy { it.quantity }
    }
}