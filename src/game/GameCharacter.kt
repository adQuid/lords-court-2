package game

import aibrain.FinishedDeal
import aibrain.ForecastBrain
import aibrain.SpecialScoreGenerator
import aibrain.Treaty
import game.action.Action
import shortstate.linetriggers.LineTrigger
import gamelogic.playerresources.GiveResource
import gamelogic.playerresources.PlayerResourceTypes
import gamelogic.resources.Resources
import scenario.MayronScoreGen
import shortstate.report.ReportFactory
import kotlin.math.min

class GameCharacter {
    val ID_NAME = "ID"
    val id: Int
    val NPC_NAME = "NPC"
    var npc: Boolean
    //This brain is still sometimes used by non-NPC characters to determine effects to deals
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
    var memory: Memory
    val ACCEPTED_DEALS_NAME = "ACCEPTEDDEALS"
    val acceptedDeals: MutableList<FinishedDeal>
    val ACCEPTED_TREATIES_NAME = "ACCEPTEDTREATIES"
    val acceptedTreaties: MutableList<Treaty>
    val WRITS_NAME = "WRITS"
    val writs: MutableList<Writ>

    val SPECIAL_LINES_NAME = "SPECIALLINES"
    val specialLines: MutableList<LineTrigger>
    val SPECIAL_SCORE_NAME = "SPECIALSCORE"
    val specialScoreGenerators: MutableList<SpecialScoreGenerator>

    val RESOURCES_NAME = "RESOURCES"
    val resources: Resources

    //testing only; starts positive to prevent infinite relative scores
    var dummyScore = 10.0

    constructor(name: String, picture: String, npc: Boolean, location: Location, game: Game) {
        this.id = game.nextID++
        this.name = name
        this.pictureString = picture
        this.npc = npc
        this.location = location
        this.memory = Memory()
        this.acceptedDeals = mutableListOf()
        this.acceptedTreaties = mutableListOf()
        this.writs = mutableListOf()
        this.specialLines = mutableListOf()
        this.specialScoreGenerators = mutableListOf()
        this.resources = Resources()
    }

    constructor(other: GameCharacter){
        this.id = other.id
        this.name = other.name
        this.pictureString = other.pictureString
        this.titles = other.titles.map{ title -> title}.toMutableSet()
        this.npc = other.npc
        this.location = other.location
        this.memory = Memory(other.memory)
        this.acceptedDeals = mutableListOf() //TODO: Fix this
        this.acceptedTreaties = mutableListOf() //TODO: Fix this
        this.writs = other.writs.map { writ -> Writ(writ) }.toMutableList()
        this.specialLines = other.specialLines //at the time of writing, special lines are immutable
        this.specialScoreGenerators = other.specialScoreGenerators //at the time of writing, score generators are immutable
        this.resources = Resources(other.resources)

        //testing only
        this.dummyScore = other.dummyScore
    }

    constructor(saveString: Map<String, Any>, game: Game){
        id = saveString[ID_NAME] as Int
        npc = saveString[NPC_NAME] as Boolean
        name = saveString[NAME_NAME] as String
        pictureString = saveString[PICTURE_NAME] as String
        titles = (saveString[TITLES_NAME] as List<Map<String, Any>>).map { map -> game.titleFromSaveString(map) }.toMutableSet()
        resources = Resources((saveString[RESOURCES_NAME] as Map<String, Any>))
        this.specialLines = (saveString[SPECIAL_LINES_NAME] as List<Map<String, Any>>).map { LineTrigger.triggerFromSaveString(it) }.toMutableList()
        this.specialScoreGenerators = (saveString[SPECIAL_SCORE_NAME] as List<Map<String, Any>>).map { SpecialScoreGenerator.scoreMap[it["type"]]!!(saveString, game) }.toMutableList()

        //To avoid circular references these are populated in finishConstruction
        location = Location(0,0)
        memory = Memory()
        acceptedDeals = mutableListOf()
        acceptedTreaties = mutableListOf()
        writs = mutableListOf()
    }

    fun finishConstruction(saveString: Map<String, Any>, game: Game){
        memory = Memory(saveString[MEMORY_NAME] as Map<String, Any>, game)
        acceptedDeals.addAll((saveString[ACCEPTED_DEALS_NAME] as List<Map<String, Any>>).map { map -> FinishedDeal(map, game)}.toMutableList())
        acceptedTreaties.addAll((saveString[ACCEPTED_TREATIES_NAME] as List<Map<String, Any>>).map { map -> Treaty(map, game)}.toMutableList())
        writs.addAll((saveString[WRITS_NAME] as List<Map<String, Any>>).map{map -> Writ(map, game)})
        location = game.locationById(saveString[LOCATION_NAME] as Int)
    }

    fun saveString(): Map<String, Any>{
        val retval = mutableMapOf<String, Any>()

        retval[ID_NAME] = id
        retval[NPC_NAME] = npc
        retval[NAME_NAME] = name
        retval[PICTURE_NAME] = pictureString
        retval[TITLES_NAME] = titles.map { title -> title.saveString() }
        retval[LOCATION_NAME] = location.id
        retval[MEMORY_NAME] = memory.saveString()
        retval[ACCEPTED_DEALS_NAME] = acceptedDeals.map { deal -> deal.saveString() }
        retval[ACCEPTED_TREATIES_NAME] = acceptedTreaties.map { treaty -> treaty.saveString() }
        retval[WRITS_NAME] = writs.map { writ -> writ.saveString() }
        retval[RESOURCES_NAME] = resources.saveString()
        retval[SPECIAL_LINES_NAME] = specialLines.map { it.saveString() }
        retval[SPECIAL_SCORE_NAME] = specialScoreGenerators.map{ it.saveString() }

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
            return name + ", " + titles.sortedByDescending { it.importance }.subList(0,min(4, titles.size)).map{ it.name }.joinToString(", ")
        } else {
            return name
        }
    }

    fun actionsReguarding(player: GameCharacter): List<Action>{
        return actionsReguarding(listOf(player))
    }

    fun actionsReguarding(players: List<GameCharacter>): List<Action>{
        val retval = titles.flatMap { title -> title.actionsReguarding(players) }
        if(players.size > 1){
            return retval.plus(PlayerResourceTypes.allTypes.filter { resources.get(it) > 0 }.map{ GiveResource(players.filter{it != this}.first().id, it, 1) })
        } else {
            return retval
        }
    }

    fun reportsEntitled(): Collection<ReportFactory>{
        return titles.map { it.reportsEntitled }.flatten()
    }

    fun lineTriggerById(id: String): LineTrigger{
        val retval = specialLines.filter { it.id == id }.firstOrNull()

        if(retval != null){
            return retval
        } else {
            throw Exception("Line trigger ${id} not found for ${name}!")
        }
    }
}