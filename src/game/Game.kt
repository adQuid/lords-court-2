package game

import game.action.Action
import game.action.GlobalActionTypeFactory
import game.action.actionTypes.BakeCookies
import game.action.actionTypes.GetMilk
import game.action.actionTypes.WasteTime
import game.gamelogicmodules.territory.TerritoryLogicModule
import game.titlemaker.CookieWorldTitleFactory
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
    val LOCATIONS_NAME = "locations"
    var locations = mutableListOf<Location>()
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

    constructor(gameLogic: Collection<GameLogicModule>){
        gameLogicModules = gameLogic
    }

    constructor(other: Game){
        isLive = other.isLive
        nextID = other.nextID
        turn = other.turn
        other.players.forEach{
            this.players.add(GameCharacter(it))
        }
        locations = other.locations.toList().map { loc -> Location(loc) }.toMutableList()
        actionsByPlayer = other.actionsByPlayer.mapValues { entry -> entry.value.toMutableList() }.toMutableMap()
        concludedPlayers = other.concludedPlayers.toMutableSet()
        titles = other.titles.map { title -> title.clone()}.toMutableSet()
        gameLogicModules = other.gameLogicModules.map { GameLogicModule.cloneModule(it, this) }
        gameLogicModules.forEach { it.finishConstruction(this) }
    }

    constructor(saveString: Map<String,Any>){
        nextID = saveString[MAX_ID_NAME] as Int
        turn = saveString[TURN_NAME] as Int
        locations = (saveString[LOCATIONS_NAME] as List<Map<String, Any>>).map { map -> Location(this, map) }.toMutableList()
        players = (saveString[PLAYERS_NAME] as List<Map<String, Any>>).map { map -> GameCharacter(map, this) }.toMutableList()

        gameLogicModules = (saveString[MODULES_NAME] as List<Map<String, Any>>).map { map -> GameLogicModule.moduleFromSaveString(map, this) }

        val tempActions = mutableMapOf<GameCharacter, MutableList<Action>>()
        (saveString[ACTIONS_NAME] as Map<Int, Any>).forEach { key, value -> tempActions[characterById(key.toInt())] =
            (value as List<Map<String,Any>>).map { map -> GlobalActionTypeFactory.fromMap(map, this) }.toMutableList() }
        actionsByPlayer = tempActions

        concludedPlayers = (saveString[CONCLUDED_PLAYERS_NAME] as List<Int>).map { id -> characterById(id) }.toMutableSet()

        titles = (saveString[TITLES_NAME] as List<Map<String, Any>>).map { map -> CookieWorldTitleFactory.titleFromSaveString(map) }.toMutableSet()

        (saveString[PLAYERS_NAME] as List<Map<String, Any>>).forEach { map -> characterById(map["ID"] as Int).finishConstruction(map, this) }
        gameLogicModules.forEach { it.finishConstruction(this) }
    }

    fun saveString(): Map<String, Any>{
        val retval = mutableMapOf<String, Any>()

        retval[MAX_ID_NAME] = nextID
        retval[TURN_NAME] = turn
        retval[PLAYERS_NAME] = players.map { it.saveString() }
        retval[LOCATIONS_NAME] = locations.map { it.saveString() }
        val saveActions = mutableMapOf<Int, List<Map<String,Any>>>()
        actionsByPlayer.forEach{(character, actions) -> saveActions[character.id] = actions.map { it.saveString() }}
        retval[ACTIONS_NAME] = saveActions
        retval[CONCLUDED_PLAYERS_NAME] = concludedPlayers.map { player -> player.id }
        retval[TITLES_NAME] = titles.map { it.saveString() }
        retval[MODULES_NAME] = gameLogicModules.map { module -> module.saveString() }

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
        if (locations != other.locations) return false
        if (concludedPlayers != other.concludedPlayers) return false
        if (titles != other.titles) return false

        return true
    }

    override fun hashCode(): Int {
        var result = nextID
        result = 31 * result + turn
        result = 31 * result + players.hashCode()
        result = 31 * result + locations.hashCode()
        result = 31 * result + actionsByPlayer.hashCode()
        result = 31 * result + concludedPlayers.hashCode()
        result = 31 * result + titles.hashCode()
        return result
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

    //this needs to return at least one action for the AI fucks up
    fun possibleActionsForPlayerReguardingPlayer(playingas: GameCharacter, reguarding: GameCharacter): List<Action>{
        var retval = ArrayList<Action>()
        if(!playingas.npc){
            retval.add(BakeCookies())
        } else {
            players.forEach { retval.add(GetMilk(it)) }
        }
        retval.add(WasteTime())
        return retval
    }

    fun appendActionsForPlayer(player: GameCharacter, actions: List<Action>){

        if(actionsByPlayer.containsKey(player)){
            actionsByPlayer[player]!!.addAll(actions)
        } else {
            actionsByPlayer[player] = actions.toMutableList()
        }
        if(isLive){
            println("$player is comitting to $actions")
            reevaluateForcastForPlayers(players.filter{it.npc})
        }
    }

    fun endTurn(): List<Effect>{
        val effects = actionsByPlayer.entries.flatMap{ entry ->
            doActions(entry.value, entry.key)
        }.plus(runLogicModules())

        effects.forEach { it.apply(this) }

        actionsByPlayer.clear()
        turn++
        return effects
    }

    private fun runLogicModules(): List<Effect>{
        val retval = mutableListOf<Effect>()
        val modulesAlreadyRun = mutableListOf<GameLogicModule>()

        var nextModule = gameLogicModules.first()
        while(modulesAlreadyRun.size < gameLogicModules.size){
            if(nextModule.dependencies.filter { !modulesAlreadyRun.contains(this.moduleOfType(it)) }.isEmpty()){
                retval.addAll(nextModule.endTurn(this))
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

    fun applyActions(actions: Collection<Action>, player: GameCharacter): List<Effect>{
        val effects = doActions(actions, player)
        effects.forEach { it.apply(this) }
        return effects
    }

    private fun doActions(actions: Collection<Action>, player: GameCharacter): List<Effect>{
        return actions.flatMap{
            it.doAction(this, player)
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

    fun reportFromMap(saveString: Map<String, Any>): Report {
        return gameLogicModules.map { module -> module.reportFromSaveString(saveString, this) }
            .filter{it != null}
            .first()!!
    }

    fun reportFromType(type: String): Report{
        return gameLogicModules.map { module -> module.reportFactoryFromType(type, this) }
            .filter{it != null}
            .first()!!.generateReport(this)
    }

    fun reportFactoryFromType(type: String): ReportFactory {
        return gameLogicModules.map { module -> module.reportFactoryFromType(type, this) }
            .filter{it != null}
            .first()!!
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
        titles.add(title)
        character.titles.add(title)
    }

    fun locationById(id: Int): Location{
        return locations.filter { it.id == id }[0]
    }
    
    fun characterById(id: Int): GameCharacter {
        return players.filter { it.id == id }[0]
    }

    fun turnName(): String {
        if(gameLogicModules.filterIsInstance<TerritoryLogicModule>().isNotEmpty()){
            return (moduleOfType(TerritoryLogicModule.type) as TerritoryLogicModule).currentWeekName()
        }
        return "Turn $turn"
    }
}