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
    var wheat: Int
    val FLOUR_NAME = "flour"
    var flour: Int
    val BREAD_NAME = "bread"
    var bread: Int

    constructor(){
        id = nextId++
        name = "Placelandia"
        wheat = 1
        flour = 2
        bread = 3
    }

    constructor(other: Territory){
        id = other.id
        name = other.name
        wheat = other.wheat
        flour = other.flour
        bread = other.bread
    }

    constructor(saveString: Map<String, Any>){
        id = saveString[ID_NAME] as Int
        name = saveString[NAME_NAME] as String
        wheat = saveString[WHEAT_NAME] as Int
        flour = saveString[FLOUR_NAME] as Int
        bread = saveString[BREAD_NAME] as Int
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
            WHEAT_NAME to wheat,
            FLOUR_NAME to flour,
            BREAD_NAME to bread
        )
    }

    fun generateCountTitle(): Title{
        return Count(this)
    }

}