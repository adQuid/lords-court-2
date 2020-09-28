package gamelogic.territory

import gamelogic.resources.ResourceTypes
import gamelogic.resources.Resources
import gamelogic.territory.mapobjects.Fleet
import gamelogic.territory.mapobjects.Structure
import kotlin.math.min

class Territory {

    companion object{
        val RESOURCES_NAME = "resources"
        val STRUCTURES_NAME = "structures"
        val FLEETS_NAME = "fleets"

        fun extractFood(resources: Resources, amount: Int): Resources{
            val foodTypesInOrder = listOf(ResourceTypes.BREAD_NAME, ResourceTypes.FISH_NAME)
            var foodLeftToExtract = amount
            val retval = Resources()

            for(type in foodTypesInOrder){
                if(foodLeftToExtract > 0 && resources.get(type) > 0){
                    retval.add(type, min(foodLeftToExtract, resources.get(type)))
                    foodLeftToExtract -= retval.get(type)
                }
            }

            return retval
        }
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
    val structures: MutableList<Structure>
    val fleets: MutableList<Fleet>

    constructor(id: Int, name: String, x: Int, y: Int){
        this.id = id
        this.name = name
        this.x = x
        this.y = y
        resources = Resources()
        resources.set(ResourceTypes.ARABLE_LAND_NAME, 200)
        resources.set(ResourceTypes.POPULATION_NAME, 100)
        resources.set(ResourceTypes.SEEDS_NAME, 100)
        resources.set(ResourceTypes.FLOUR_NAME, 1000)
        resources.set(ResourceTypes.BREAD_NAME, 100)
        crops = mutableListOf()
        lastHarvest = listOf()
        structures = mutableListOf()
        fleets = mutableListOf()
    }

    constructor(other: Territory){
        id = other.id
        name = other.name
        x = other.x
        y = other.y
        resources = Resources(other.resources)
        crops = other.crops.map { Crop(it) }.toMutableList()
        lastHarvest = other.lastHarvest //don't need to deep copy since it's not mutable
        structures = other.structures.map{Structure(it)}.toMutableList()
        fleets = other.fleets.map{Fleet(it)}.toMutableList()
    }

    constructor(saveString: Map<String, Any>){
        id = saveString[ID_NAME] as Int
        name = saveString[NAME_NAME] as String
        x = saveString["x"] as Int
        y = saveString["y"] as Int
        resources = Resources(saveString[RESOURCES_NAME] as MutableMap<String, Any>)
        crops = (saveString[CROPS_NAME] as List<Map<String, Any>>).map{map -> Crop(map)}.toMutableList()
        lastHarvest = (saveString[LAST_HARVEST_NAME] as List<Map<String,Any>>).map{map -> Crop(map)}
        structures = (saveString[STRUCTURES_NAME] as List<Map<String, Any>>).map { map -> Structure(map) }.toMutableList()
        fleets = (saveString[FLEETS_NAME] as List<Map<String, Any>>).map{ map -> Fleet(map)}.toMutableList()
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
            LAST_HARVEST_NAME to lastHarvest.map{ it.saveString() },
            STRUCTURES_NAME to structures.map { it.saveString() },
            FLEETS_NAME to fleets.map { it.saveString() }
        )
    }

    fun modifyResource(resource: String, value: Int){
        resources.add(resource, value)
    }

    fun totalCropsPlanted(): Int{
        return crops.sumBy { it.quantity }
    }

    fun foodNeededNextTurn(): Int{
        return resources.get(ResourceTypes.POPULATION_NAME)
    }

    fun foodToEatNextTurn(): Resources {
        return extractFood(resources, resources.get(ResourceTypes.POPULATION_NAME))
    }
}