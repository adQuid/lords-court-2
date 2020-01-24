package game

import aibrain.Deal
import aibrain.ForecastBrain
import aibrain.Treaty
import shortstate.dialog.Memory

class GameCharacter {
    val ID_NAME = "ID"
    val id: Int
    val NPC_NAME = "NPC"
    var npc: Boolean
    var brain = ForecastBrain(this)

    val NAME_NAME = "NAME"
    val name: String
    val PICTURE_NAME = "PICTURESTRING"
    val pictureString: String
    val TITLES_NAME = "TITLES"
    var titles = mutableSetOf<Title>()

    val LOCATION_NAME = "LOCATION"
    var location: Location

    val MEMORY_NAME = "MEMORY"
    val memory: MutableList<Memory>
    val ACCEPTED_DEALS_NAME = "ACCEPTEDDEALS"
    val acceptedDeals: MutableList<Deal>
    val ACCEPTED_TREATIES_NAME = "ACCEPTEDTREATIES"
    val acceptedTreaties: MutableList<Treaty>

    //testing only
    var dummyScore = 0.0

    constructor(name: String, picture: String, npc: Boolean, location: Location, game: Game) {
        this.id = game.nextID++
        this.name = name
        this.pictureString = picture
        this.npc = npc
        this.location = location
        this.memory = mutableListOf()
        this.acceptedDeals = mutableListOf()
        this.acceptedTreaties = mutableListOf()
    }

    constructor(other: GameCharacter){
        this.id = other.id
        this.name = other.name
        this.pictureString = other.pictureString
        this.npc = other.npc
        this.location = other.location
        this.memory = other.memory.map{ memory -> Memory(memory)}.toMutableList()
        this.acceptedDeals = mutableListOf()
        this.acceptedTreaties = mutableListOf()

        //testing only
        this.dummyScore = other.dummyScore
    }

    constructor(saveString: Map<String, Any>, game: Game){
        id = saveString[ID_NAME] as Int
        npc = saveString[NPC_NAME] as Boolean
        name = saveString[NAME_NAME] as String
        pictureString = saveString[PICTURE_NAME] as String
        titles = (saveString[TITLES_NAME] as List<Map<String, Any>>).map { map -> Title(map) }.toMutableSet()
        location = game.locationById(saveString[LOCATION_NAME] as Int)
        //To avoid circular references these are populated in finishConstruction
        memory = mutableListOf()
        acceptedDeals = mutableListOf()
        acceptedTreaties = mutableListOf()
    }

    fun finishConstruction(saveString: Map<String, Any>, game: Game){
        memory.addAll((saveString[MEMORY_NAME] as List<Map<String, Any>>).map { map -> Memory(map, game)})
        acceptedDeals.addAll((saveString[ACCEPTED_DEALS_NAME] as List<Map<String, Any>>).map { map -> Deal(map, game)}.toMutableList())
        acceptedTreaties.addAll((saveString[ACCEPTED_TREATIES_NAME] as List<Map<String, Any>>).map { map -> Treaty(map, game)}.toMutableList())
    }

    fun saveString(): Map<String, Any>{
        val retval = mutableMapOf<String, Any>()

        retval[ID_NAME] = id
        retval[NPC_NAME] = npc
        retval[NAME_NAME] = name
        retval[PICTURE_NAME] = pictureString
        retval[TITLES_NAME] = titles.map { title -> title.saveString() }
        retval[LOCATION_NAME] = location.id
        retval[MEMORY_NAME] = memory.map { memory -> memory.saveString() }
        retval[ACCEPTED_DEALS_NAME] = acceptedDeals.map { deal -> deal.saveString() }
        retval[ACCEPTED_TREATIES_NAME] = acceptedTreaties.map { treaty -> treaty.saveString() }

        return retval
    }

    override fun equals(other: Any?): Boolean {
        if(other is GameCharacter){
            return this.id == other.id
        }
        return false
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    override fun toString(): String{
        return name
    }

    fun fullName(): String{
        if(titles.isNotEmpty()){
            return name + ", " + titles.joinToString(", ")
        } else {
            return name
        }
    }
}