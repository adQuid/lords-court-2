package game

import aibrain.Deal
import game.action.Action
import game.action.GlobalActionTypeFactory
import game.culture.Culture
import gamelogic.government.GovernmentLogicModule
import gamelogic.resources.Resources
import gamelogic.territory.TerritoryLogicModule
import main.UIGlobals
import shortstate.report.Report
import shortstate.report.ReportFactory


class Game {
    var isLive = true
    val MAX_ID_NAME = "maxId"
    var nextID = 1 //this is for all kinds of objects; no need to seperate
    val TURN_NAME = "turn"
    var turn = 1
    val PLAYERS_NAME = "players"
    var players = mutableListOf<GameCharacter>()
    val ACTIONS_NAME = "actions"
    var actionsByPlayer = mutableMapOf<GameCharacter, MutableList<Action>>()
    val CONCLUDED_PLAYERS_NAME = "concludedPlayers"
    var concludedPlayers = mutableSetOf<GameCharacter>()

    //This is more of a "secondary" pointer, and is expected to be duplicated with anyone actually holding the titles.
    //These exist to prevent titles from vanishing when nobody is holding them
    val TITLES_NAME = "titles"
    var titles = mutableSetOf<Title>()

    val MODULES_NAME = "modules"
    val gameLogicModules: Collection<GameLogicModule> //TODO: Make this mutable
    val CULTURES_NAME = "cultures"
    val cultures: MutableSet<Culture>

    val SPECIAL_SCRIPTS_NAME = "specialscripts"
    val specialScriptClass: String
    val specialScripts: Collection<SpecialScript>

    constructor(gameLogic: Collection<GameLogicModule>): this(gameLogic, "")

    constructor(gameLogic: Collection<GameLogicModule>, speicalScripts: String){
        gameLogicModules = gameLogic
        cultures = mutableSetOf()
        specialScriptClass = speicalScripts
        specialScripts = specialScriptsFromId(specialScriptClass)
        setModuleParents()
    }

    constructor(other: Game){
        isLive = other.isLive
        nextID = other.nextID
        turn = other.turn

        cultures = other.cultures.map { Culture(it) }.toMutableSet()

        other.players.forEach{
            this.players.add(GameCharacter(it, this))
        }
        actionsByPlayer = other.actionsByPlayer.mapValues { entry -> entry.value.toMutableList() }.toMutableMap()
        concludedPlayers = other.concludedPlayers.toMutableSet()
        titles = other.titles.map { title -> title.clone()}.toMutableSet()
        gameLogicModules = other.logicModulesInDependencyOrder().map { GameLogicModule.cloneModule(it, this) }
        gameLogicModules.forEach { it.finishConstruction(this) }
        specialScriptClass = other.specialScriptClass
        specialScripts = other.specialScripts //these are not mutable
        setModuleParents()
    }

    constructor(saveString: Map<String,Any>){
        gameLogicModules = (saveString[MODULES_NAME] as List<Map<String, Any>>).map { map -> GameLogicModule.moduleFromSaveString(map, this) }
        cultures = (saveString[CULTURES_NAME] as List<Map<String, Any>>).map{Culture(it)}.toMutableSet()
        specialScriptClass = saveString[SPECIAL_SCRIPTS_NAME] as String
        specialScripts = specialScriptsFromId(specialScriptClass)
        nextID = saveString[MAX_ID_NAME] as Int
        turn = saveString[TURN_NAME] as Int
        players = (saveString[PLAYERS_NAME] as List<Map<String, Any>>).map { map -> GameCharacter(map, this) }.toMutableList()

        val tempActions = mutableMapOf<GameCharacter, MutableList<Action>>()
        (saveString[ACTIONS_NAME] as Map<String, Any>).forEach { key, value -> tempActions[characterById(Integer.parseInt(key))] =
            (value as List<Map<String,Any>>).map { map -> GlobalActionTypeFactory.fromMap(map, this) }.toMutableList() }
        actionsByPlayer = tempActions

        concludedPlayers = (saveString[CONCLUDED_PLAYERS_NAME] as List<Int>).map { id -> characterById(id) }.toMutableSet()

        titles = (saveString[TITLES_NAME] as List<Map<String, Any>>).map { map -> titleFromSaveString(map) }.toMutableSet()

        logicModulesInDependencyOrder().forEach { it.finishConstruction(this) }
        (saveString[PLAYERS_NAME] as List<Map<String, Any>>).forEach { map -> characterById(map["ID"] as Int).finishConstruction(map, this) }
        setModuleParents()
    }

    private fun setModuleParents(){
        gameLogicModules.forEach {
            it.parent = this
        }
    }

    fun saveString(): Map<String, Any>{
        val retval = mutableMapOf<String, Any>()

        retval[MAX_ID_NAME] = nextID
        retval[TURN_NAME] = turn
        retval[PLAYERS_NAME] = players.map { it.saveString() }
        val saveActions = mutableMapOf<String, List<Map<String,Any>>>()
        actionsByPlayer.forEach{(character, actions) -> saveActions["${character.id}"] = actions.map { it.saveString() }}
        retval[ACTIONS_NAME] = saveActions
        retval[CONCLUDED_PLAYERS_NAME] = concludedPlayers.map { player -> player.id }
        retval[TITLES_NAME] = titles.map { it.saveString() }
        retval[MODULES_NAME] = gameLogicModules.map { module -> module.saveString() }
        retval[CULTURES_NAME] = cultures.map { it.saveString() }
        retval[SPECIAL_SCRIPTS_NAME] = specialScriptClass

        return retval
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Game

        if (actionsByPlayer != other.actionsByPlayer) return false
        if (nextID != other.nextID) return false
        if (turn != other.turn) return false
        if (players != other.players) return false
        if (locations() != other.locations()) return false
        if (concludedPlayers != other.concludedPlayers) return false
        if (titles != other.titles) return false
        if (cultures != other.cultures) return false

        return true
    }

    override fun hashCode(): Int {
        var result = nextID
        result = 31 * result + turn
        result = 31 * result + players.hashCode()
        result = 31 * result + actionsByPlayer.hashCode()
        result = 31 * result + concludedPlayers.hashCode()
        result = 31 * result + titles.hashCode()
        return result
    }

    fun locations(): Collection<Location> {
        return gameLogicModules.flatMap { it.locations() }
    }

    fun imageFor(player: GameCharacter): Game{
        var retval = Game(this)
        retval.isLive = false

        players.forEach{
            if(it != player){
                //retval.actionsByPlayer[it] = mutableListOf()
            }
        }
        return retval
    }

    fun appendActionsForPlayer(player: GameCharacter, actions: List<Action>){

        if(!actionsByPlayer.containsKey(player)){
            actionsByPlayer[player] = actions.toMutableList()

        } else {
            actionsByPlayer[player] = actionsByPlayer[player]!!.filter { actions.none { action -> action.collidesWith(it) } }.toMutableList()
            actionsByPlayer[player]!!.addAll(actions)
        }
        if(isLive){
            println("$player is comitting to $actions")
            players.filter{it.npc && it.location == player.location}.forEach{it.brain.lastCasesOfConcern = null}
        }
    }

    fun endTurn(){
        concludedPlayers.clear()

        specialScripts.filter{it.turnToActivate == turn}.forEach { it.doscript(this) }

        actionsByPlayer = actionsByPlayer.filter { it.key in players }.toMutableMap()
        actionsByPlayer.entries.forEach{ entry ->
            doActions(entry.value, entry.key)
        }

        runLogicModules()

        actionsByPlayer.clear()
        turn++
    }

    private fun runLogicModules(){
        logicModulesInDependencyOrder().forEach { it.endTurn(this) }
    }

    private fun logicModulesInDependencyOrder(): List<GameLogicModule>{
        val retval = mutableListOf<GameLogicModule>()
        val modulesAlreadyRun = mutableListOf<GameLogicModule>()

        var nextModule = gameLogicModules.filter { it.dependencies.isEmpty() }.first()
        while(modulesAlreadyRun.size < gameLogicModules.size){
            if(nextModule.dependencies.filter { !modulesAlreadyRun.contains(this.moduleOfType(it)) }.isEmpty()){
                retval.add(nextModule)
                modulesAlreadyRun.add(nextModule)
                if(modulesAlreadyRun.size < gameLogicModules.size){
                    nextModule = gameLogicModules.filter { !modulesAlreadyRun.contains(it) }.first()
                }
            } else {
                nextModule = moduleOfType(nextModule.dependencies.filter { !modulesAlreadyRun.contains(this.moduleOfType(it)) }.first())
            }
        }

        return retval
    }

    private fun doActions(actions: Collection<Action>, player: GameCharacter){
    actions.forEach{
            it.doAction(this, matchingPlayer(player)!!)
        }
    }

    fun addPlayer(player: GameCharacter){
        players.add(player)
        player.titles.forEach{ title -> titles.add(title)}
    }

    fun matchingPlayer(player: GameCharacter): GameCharacter?{
        players.forEach { if(it == player){ return it} }
        return null
    }

    fun moduleOfType(type: String): GameLogicModule{
        return gameLogicModules.filter { it.type == type }.first()
    }

    //honestly, I don't like that this is EVER used
    fun hasModuleOfType(type: String): Boolean{
        return gameLogicModules.filter { it.type == type }.isNotEmpty()
    }

    fun reportFromMap(saveString: Map<String, Any>): Report {
        return gameLogicModules.map { module -> module.reportFromSaveString(saveString, this) }
            .filterNotNull()
            .first()!!
    }

    fun reportFactoryFromType(type: String): ReportFactory {
        return gameLogicModules.map { module -> module.reportFactoryFromType(type, this) }
            .filterNotNull()
            .first()!!
    }

    fun titleFromSaveString(saveString: Map<String, Any>): Title {
        return gameLogicModules.map{ module -> module.titleTypes.titleFromSaveString(saveString, this)}.filter { it != null }.map{it!!}.first()
    }

    private fun reevaluateForcastForPlayers(players: List<GameCharacter>){
        players.forEach {
            it.brain.thinkAboutNextTurn(this)
        }
    }

     @Synchronized fun nextPlayerToForcast(): GameCharacter?{
         for (player in players) {
                 if (player.brain.lastCasesOfConcern == null && (player.npc)) {
                     return player
                 }
         }
        return null
    }

    fun playersAtLocation(location: Location): List<GameCharacter>{
        return players.filter { player -> player.location == location }
    }

    fun playerCharacter(): GameCharacter {
        players.forEach {
            if(!it.npc){
                return it
            }
        }
        throw Exception("No player found!")
    }

    fun applyTitleToCharacter(title: Title, character: GameCharacter){
        if(!titles.contains(title)){
            titles.add(title)
        }
        character.titles.add(title)
    }

    fun transferTitle(giver: GameCharacter, recipient: GameCharacter, title: Title){
        if(!titles.contains(title)){
            titles.add(title)
        }
        if(giver.titles.contains(title)){
            applyTitleToCharacter(title, recipient)
            giver.titles.remove(title)
        }

    }

    fun applyCultureToCharacter(name: String, character: GameCharacter){
        character.addCulture(name, this)
    }

    fun locationById(id: Int): Location{
        return locations().filter { it.id == id }[0]
    }
    
    fun characterById(id: Int): GameCharacter {
        return players.filter { it.id == id }[0]
    }

    fun characterByName(name: String): GameCharacter {
        return players.filter { it.name == name }[0]
    }

    fun cultureByName(name: String): Culture {
        return cultures.filter { it.name == name }.first()
    }

    fun resourcesByCharacter(gameCharacter: GameCharacter): Resources {
        val governmentLogic = gameLogicModules.filter { it is GovernmentLogicModule }.map{it as GovernmentLogicModule }.firstOrNull()

        var retval = Resources(gameCharacter.privateResources)
        if(governmentLogic != null){
            val capitalWherePlayerIs = governmentLogic.capitalByLocation(gameCharacter.location)
            if(capitalWherePlayerIs != null){
                if(governmentLogic.countOfCaptial(capitalWherePlayerIs!!.terId) == gameCharacter){
                    retval = retval.plus(capitalWherePlayerIs!!.resources)
                }
            } else {
                error("Trying to add resources for character not at a capital!")
            }
        }
        return retval
    }

    fun addResourceForCharacter(gameCharacter: GameCharacter, resources: Resources){
        val governmentLogic = gameLogicModules.filter { it is GovernmentLogicModule }.map{it as GovernmentLogicModule }.firstOrNull()

        if(governmentLogic != null){
            val capitalWherePlayerIs = governmentLogic.capitalByLocation(gameCharacter.location)
            if(capitalWherePlayerIs != null){
                if(governmentLogic.countOfCaptial(capitalWherePlayerIs.terId) == gameCharacter){
                    capitalWherePlayerIs.resources.addAll(resources)
                    return
                }
            } else {
                error("Trying to add resources for character not at a capital!")
            }
        }

        gameCharacter.privateResources.addAll(resources)
    }

    fun turnName(): String {
        if(gameLogicModules.filterIsInstance<TerritoryLogicModule>().isNotEmpty()){
            return (moduleOfType(TerritoryLogicModule.type) as TerritoryLogicModule).currentWeekName()
        }
        return "Turn $turn"
    }
}