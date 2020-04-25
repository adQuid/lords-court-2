package game

import game.action.Action
import game.action.GlobalActionTypeFactory
import game.action.actionTypes.BakeCookies
import game.action.actionTypes.GetMilk
import game.action.actionTypes.WasteTime
import game.titlemaker.TitleFactory


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

    //temporary stats
    val DELICIOUSNESS_NAME = "deliciousness"
    var deliciousness = 0.0
    val HAS_MILK_NAME = "hasMilk"
    var hasMilk = mutableListOf<GameCharacter>()

    constructor(){

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


        deliciousness = other.deliciousness
        hasMilk = other.hasMilk.toMutableList()
    }

    constructor(saveString: Map<String,Any>){
        nextID = saveString[MAX_ID_NAME] as Int
        turn = saveString[TURN_NAME] as Int
        locations = (saveString[LOCATIONS_NAME] as List<Map<String, Any>>).map { map -> Location(this, map) }.toMutableList()

        players = (saveString[PLAYERS_NAME] as List<Map<String, Any>>).map { map -> GameCharacter(map, this) }.toMutableList()
        (saveString[PLAYERS_NAME] as List<Map<String, Any>>).forEach { map -> characterById(map["ID"] as Int).finishConstruction(map, this) }

        val tempActions = mutableMapOf<GameCharacter, MutableList<Action>>()
        (saveString[ACTIONS_NAME] as Map<Int, Any>).forEach { key, value -> tempActions[characterById(key.toInt())] =
            (value as List<Map<String,Any>>).map { map -> GlobalActionTypeFactory.fromMap(map, this) }.toMutableList() }
        actionsByPlayer = tempActions

        concludedPlayers = (saveString[CONCLUDED_PLAYERS_NAME] as List<Int>).map { id -> characterById(id) }.toMutableSet()

        titles = (saveString[TITLES_NAME] as List<Map<String, Any>>).map { map -> TitleFactory.titleFromSaveString(map) }.toMutableSet()

        deliciousness = saveString[DELICIOUSNESS_NAME] as Double
        hasMilk = (saveString[HAS_MILK_NAME] as List<Int>).map { id -> characterById(id)}.toMutableList()
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

        retval[DELICIOUSNESS_NAME] = deliciousness
        retval[HAS_MILK_NAME] = hasMilk.map { it.id }
        
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
        if (deliciousness != other.deliciousness) return false
        if (hasMilk != other.hasMilk) return false

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
        result = 31 * result + deliciousness.hashCode()
        result = 31 * result + hasMilk.hashCode()
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
            reevaluateForcastForPlayers(listOf(player))
        }
    }

    fun endTurn(): List<Effect>{
        val effects = actionsByPlayer.entries.flatMap{ entry ->
            doActions(entry.value, entry.key)
        }

        effects.forEach { it.apply(this) }

        actionsByPlayer.clear()
        turn++
        return effects
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

}