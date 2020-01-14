package game

import game.action.Action
import game.action.GlobalActionTypeFactory
import game.action.actionTypes.BakeCookies
import game.action.actionTypes.GetMilk
import game.action.actionTypes.WasteTime


class Game {
    var isLive = true
    val MAX_ID_NAME = "maxId"
    var nextID = 1 //this is for all kinds of objects; no need to seperate
    val TURN_NAME = "turn"
    var turn = 1
    val PLAYERS_NAME = "players"
    var players = mutableListOf<Character>()
    val LOCATIONS_NAME = "locations"
    var locations = mutableListOf<Location>()
    val ACTIONS_NAME = "actions"
    var actionsByPlayer = mutableMapOf<Character, List<Action>>()

    //This is more of a "secondary" pointer, and is expected to be duplicated with anyone actually holding the titles.
    //These exist to prevent titles from vanishing when nobody is holding them
    val TITLES_NAME = "titles"
    var titles = mutableSetOf<Title>()

    //temporary stats
    val DELICIOUSNESS_NAME = "deliciousness"
    var deliciousness = 0.0
    val HAS_MILK_NAME = "hasMilk"
    var hasMilk = mutableListOf<Character>()

    constructor(){

    }

    constructor(other: Game){
        isLive = false
        turn = other.turn
        other.players.forEach{
            this.players.add(Character(it))
        }
        locations = other.locations.toList().map { loc -> Location(loc) }.toMutableList()
        titles = other.titles.map { title -> Title(title)}.toMutableSet()
        deliciousness = other.deliciousness
        hasMilk = other.hasMilk.toMutableList()
    }

    constructor(saveString: Map<String,Any>){
        nextID = saveString[MAX_ID_NAME] as Int
        turn = saveString[TURN_NAME] as Int
        locations = (saveString[LOCATIONS_NAME] as List<Map<String, Any>>).map { map -> Location(map, this) }.toMutableList()
        players = (saveString[PLAYERS_NAME] as List<Map<String, Any>>).map { map -> Character(map, this) }.toMutableList()

        val tempActions = mutableMapOf<Character, List<Action>>()
        (saveString[ACTIONS_NAME] as Map<String, Any>).forEach { key, value -> tempActions[characterById(key.toInt())] =
            (value as List<Map<String,Any>>).map { map -> Action(GlobalActionTypeFactory.fromMap(map)) } }
        actionsByPlayer = tempActions

        titles = (saveString[TITLES_NAME] as List<Map<String, Any>>).map { map -> Title(map) }.toMutableSet()

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
        retval[TITLES_NAME] = titles.map { it.saveString() }

        retval[DELICIOUSNESS_NAME] = deliciousness
        retval[HAS_MILK_NAME] = hasMilk.map { it.id }
        
        return retval
    }

    fun imageFor(player: Character): Game{
        var retval = Game(this)

        players.forEach{
            retval.actionsByPlayer[it] = listOf()
        }
        return retval
    }

    //this needs to return at least one action for the AI fucks up
    fun possibleActionsForPlayer(player: Character): List<Action>{
        var retval = ArrayList<Action>()
        if(!player.npc){
            retval.add(Action(BakeCookies()))
        } else {
            players.forEach { retval.add(Action(GetMilk(it))) }
        }
        retval.add(Action(WasteTime()))
        return retval
    }

    fun commitActionsForPlayer(player: Character, actions: List<Action>){
        if(isLive){
            println("$player is comitting to $actions")
        }
        actionsByPlayer[player] = actions
    }

    fun endTurn(){
        actionsByPlayer.entries.forEach{ entry ->
            applyActions(entry.value, entry.key)
        }

        actionsByPlayer.clear()

        turn++
    }

    fun applyActions(actions: Collection<Action>, player: Character){
        actions.forEach {
            it.type.doAction(this, player).forEach{
                it.apply(this)
            }
        }
    }

    fun matchingPlayer(player: Character): Character?{
        players.forEach { if(it == player){ return it} }
        return null
    }

     @Synchronized fun nextPlayerToForcast(): Character?{
         for (player in players) {
                 if (player.brain.lastCasesOfConcern == null && (player.npc)) {
                     return player
                 }
         }
        return null
    }

    fun playersAtLocation(location: Location): List<Character>{
        return players.filter { player -> player.location == location }
    }

    fun playerCharacter(): Character {
        players.forEach {
            if(!it.npc){
                return it
            }
        }
        throw Exception("No player found!")
    }

    fun applyTitleToCharacter(title: Title, character: Character){
        titles.add(title)
        character.titles.add(title)
    }

    fun locationById(id: Int): Location{
        return locations.filter { it.id == id }[0]
    }
    
    fun characterById(id: Int): Character {
        return players.filter { it.id == id }[0]
    }
}