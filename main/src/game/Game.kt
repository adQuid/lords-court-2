package game

import game.action.Action
import game.action.actionTypes.BakeCookies
import game.action.actionTypes.GetMilk
import game.action.actionTypes.WasteTime

class Game {
    var isLive = true
    var turn = 1
    var players = mutableListOf<Character>()
    var locations = mutableListOf<Location>()
    var actionsByPlayer = HashMap<Character, List<Action>>()

    //This is more of a "secondary" pointer, and is expected to be duplicated with anyone actually holding the titles.
    //These exist to prevent titles from vanishing when nobody is holding them
    var titles = mutableSetOf<Title>()

    //temporary stats
    var deliciousness = 0.0
    var hasPlate = mutableListOf<Character>()

    constructor(){


        /*val NPC2 = Character("Omrin", "assets//portraits//stacheman.png", true, locations[0])
        players.add(NPC2)*/
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
            retval.add(Action(GetMilk()))
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
}