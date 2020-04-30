package game.gamelogicmodules.territory

import game.Title

class Territory {

    companion object{
        var nextId = 0
    }

    val ID_NAME = "ID"
    val id: Int
    val NAME_NAME = "name"
    val name: String
    val WHEAT_NAME = "wheat"
    val FLOUR_NAME = "flour"
    val BREAD_NAME = "bread"
    val RESOURCES_NAME = "resources"
    val resources: MutableMap<String, Int>

    constructor(){
        id = nextId++
        name = "Placelandia"
        resources = mutableMapOf(
        WHEAT_NAME to 1,
        FLOUR_NAME to 2,
        BREAD_NAME to 3
        )
    }

    constructor(other: Territory){
        id = other.id
        name = other.name
        resources = other.resources.toMutableMap()
    }

    constructor(saveString: Map<String, Any>){
        id = saveString[ID_NAME] as Int
        name = saveString[NAME_NAME] as String
        resources = saveString[RESOURCES_NAME] as MutableMap<String, Int>
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
            RESOURCES_NAME to resources
        )
    }

    fun generateCountTitle(): Title{
        return Count(this)
    }

}