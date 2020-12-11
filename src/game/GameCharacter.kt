package game

import aibrain.FinishedDeal
import aibrain.ForecastBrain
import aibrain.SpecialScoreGenerator
import aibrain.Treaty
import game.action.Action
import game.culture.*
import shortstate.linetriggers.LineTrigger
import gamelogic.playerresources.GiveResource
import gamelogic.playerresources.PlayerResourceTypes
import gamelogic.resources.ResourceTypes
import gamelogic.resources.Resources
import shortstate.dialog.GlobalLineTypeFactory
import shortstate.dialog.Line
import shortstate.report.Report
import shortstate.report.ReportFactory
import ui.specialdisplayables.selectionmodal.Tab
import kotlin.math.min

class GameCharacter {

    companion object{
        val ID_NAME = "ID"
        val NPC_NAME = "NPC"
        val NAME_NAME = "NAME"
        val PICTURE_NAME = "PICTURESTRING"
        val TITLES_NAME = "TITLES"
        val LOCATION_NAME = "LOCATION"
        val MEMORY_NAME = "MEMORY"
        val ACCEPTED_DEALS_NAME = "ACCEPTEDDEALS"
        val ACCEPTED_TREATIES_NAME = "ACCEPTEDTREATIES"
        val WRITS_NAME = "WRITS"
        val PETITIONS_NAME = "PETITIONS"
        val REPORTS_NAME = "REPORTS"
        val CULTURES_NAME = "CULTURES"
        val SPECIAL_LINES_NAME = "SPECIALLINES"
        val SPECIAL_SCORE_NAME = "SPECIALSCORE"
        val RESOURCES_NAME = "RESOURCES"
    }


    val id: Int

    var npc: Boolean
    //This brain is still sometimes used by non-NPC characters to determine effects to deals
    var brain: ForecastBrain

    val name: String
    val pictureString: String
    var titles = mutableSetOf<Title>()

    var location: Location

    var memory: Memory
    var knownReports = mutableListOf<Report>()
    val acceptedDeals: MutableList<FinishedDeal>
    val acceptedTreaties: MutableList<Treaty>
    val writs: MutableList<Writ>
    val petitions: MutableList<Line>

    private val cultures: MutableList<Culture>
    val specialLines: MutableList<LineTrigger>
    val specialScoreGenerators: MutableList<SpecialScoreGenerator>

    val privateResources: Resources

    //testing only; starts positive to prevent infinite relative scores
    var dummyScore = 10.0

    constructor(name: String, picture: String, npc: Boolean, location: Location, game: Game) {
        this.id = game.nextID++
        this.name = name
        this.brain = ForecastBrain(this)
        this.pictureString = picture
        this.npc = npc
        this.location = location
        this.memory = Memory()
        this.acceptedDeals = mutableListOf()
        this.acceptedTreaties = mutableListOf()
        this.writs = mutableListOf()
        this.petitions = mutableListOf()
        this.cultures = mutableListOf()
        this.specialLines = mutableListOf()
        this.specialScoreGenerators = mutableListOf()
        this.privateResources = Resources()
    }

    constructor(other: GameCharacter, game: Game){
        this.id = other.id
        this.name = other.name
        this.brain = ForecastBrain(other.brain, this)
        this.pictureString = other.pictureString
        this.titles = other.titles.map{ title -> title}.toMutableSet()
        this.npc = other.npc
        this.location = other.location
        this.memory = Memory(other.memory)
        this.acceptedDeals = mutableListOf() //TODO: Fix this
        this.acceptedTreaties = mutableListOf() //TODO: Fix this
        this.writs = other.writs.map { writ -> Writ(writ) }.toMutableList()
        this.petitions = other.petitions.toMutableList()
        this.cultures = other.cultures.map { game.cultureByName(it.name) }.toMutableList()
        this.specialLines = other.specialLines //at the time of writing, special lines are immutable
        this.specialScoreGenerators = other.specialScoreGenerators //at the time of writing, score generators are immutable
        this.privateResources = Resources(other.privateResources)

        //testing only
        this.dummyScore = other.dummyScore
    }

    constructor(saveString: Map<String, Any>, game: Game){
        id = saveString[ID_NAME] as Int
        npc = saveString[NPC_NAME] as Boolean
        name = saveString[NAME_NAME] as String
        brain = ForecastBrain(this)
        pictureString = saveString[PICTURE_NAME] as String
        titles = (saveString[TITLES_NAME] as List<Map<String, Any>>).map { map -> game.titleFromSaveString(map) }.toMutableSet()
        privateResources = Resources((saveString[RESOURCES_NAME] as Map<String, Any>))
        this.cultures = (saveString[CULTURES_NAME] as List<String>).map { game.cultureByName(it) }.toMutableList()
        this.specialLines = (saveString[SPECIAL_LINES_NAME] as List<Map<String, Any>>).map { LineTrigger.triggerFromSaveString(it) }.toMutableList()
        this.specialScoreGenerators = (saveString[SPECIAL_SCORE_NAME] as List<Map<String, Any>>).map { SpecialScoreGenerator.scoreMap[it["type"]]!!(saveString, game) }.toMutableList()

        //To avoid circular references these are populated in finishConstruction
        location = Location(0,0)
        memory = Memory()
        acceptedDeals = mutableListOf()
        acceptedTreaties = mutableListOf()
        knownReports = mutableListOf()
        writs = mutableListOf()
        petitions = mutableListOf()
    }

    fun finishConstruction(saveString: Map<String, Any>, game: Game){
        memory = Memory(saveString[MEMORY_NAME] as Map<String, Any>, game)
        acceptedDeals.addAll((saveString[ACCEPTED_DEALS_NAME] as List<Map<String, Any>>).map { map -> FinishedDeal(map, game)}.toMutableList())
        acceptedTreaties.addAll((saveString[ACCEPTED_TREATIES_NAME] as List<Map<String, Any>>).map { map -> Treaty(map, game)}.toMutableList())
        knownReports.addAll((saveString[REPORTS_NAME] as List<Map<String, Any>>).map { map -> game.reportFromMap(map) }.toMutableList())
        writs.addAll((saveString[WRITS_NAME] as List<Map<String, Any>>).map{map -> Writ(map, game)})
        petitions.addAll((saveString[PETITIONS_NAME] as List<Map<String, Any>>).map { GlobalLineTypeFactory.fromMap(it, game) })
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
        retval[PETITIONS_NAME] = petitions.map { it.saveString() }
        retval[REPORTS_NAME] = knownReports.map{ report -> report.saveString()}
        retval[RESOURCES_NAME] = privateResources.saveString()
        retval[CULTURES_NAME] = cultures.map{it.name}
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

    fun addCulture(culture: String, game: Game){
        cultures.add(game.cultureByName(culture))
    }

    fun topics(): Set<Topic>{
        return cultures.fold(mutableSetOf(), {acc, culture -> culture.topics.forEach { if(acc.filter { topic -> topic.name == it.name }.isEmpty()) acc.add(it) }; acc}) //.fold(mutableSetOf(), {acc, cul -> cul.topics.forEach{if(!acc.contains(it)) acc[it.key] = it.value}; return acc})
    }

    fun infoOnTopic(topic: String): String?{
        val topics = topics()
        return topics.filter { it.name == topic }.firstOrNull()?.description
    }

    fun reportOfType(type: String): Report? {
        return knownReports.filter { report -> report.type == type }.sortedByDescending { it.turn }.getOrNull(0)
    }

    fun recentReports(turn: Int): List<Report> {
        return knownReports.filter { it.turn == turn }
    }

    fun fullName(): String{
        if(titles.isNotEmpty()){
            return name + ", " + titles.sortedByDescending { it.importance }.subList(0,min(4, titles.size)).map{ it.name }.joinToString(", ")
        } else {
            return name
        }
    }

    fun actionsReguarding(game: Game, player: GameCharacter): List<Action>{
        return actionsReguarding(game, listOf(player))
    }

    fun actionsReguarding(game: Game, players: List<GameCharacter>): List<Action>{
        return actionTabsRegarding(game, players).flatMap { it.items }
    }

    fun actionTabsRegarding(game: Game, players: List<GameCharacter>): List<Tab<Action>>{
        var retval = titles.flatMap { title -> title.actionsReguarding(players)}
        if(players.size > 1){
            val temp = ResourceTypes.tradableTypes.filter { game.resourcesByCharacter(this).get(it) > 0 }.map{ GiveResource(players.filter{it != this}.first().id, it, 1) }
            val tab = Tab<Action>("Resource Transfers", temp)
            retval = retval.plus(listOf(tab))
        }

        return retval.filter { it.items.isNotEmpty() }
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