package game

import action.Action
import actionTypes.BakeCookies
import actionTypes.WasteTime
import aibrain.Plan

class Game {
    var isLive = true
    var turn = 1
    var players = mutableListOf<Player>()
    var locations = listOf(Location())
    var actionsByPlayer = HashMap<Player, List<Action>>()

    //temporary stat
    var deliciousness = 0

    constructor(){
        players.add(Player("Melkar the Magnificant", false, locations[0]))
        players.add(Player("npc", true, locations[0]))
        
    }

    constructor(other: Game){
        isLive = false
        other.players.forEach{
            this.players.add(Player(it))
        }
    }

    fun imageFor(player: Player): Game{
        var retval = Game(this)

        players.forEach{
            retval.actionsByPlayer[it] = listOf()
        }
        return retval
    }

    fun possibleActionsForPlayer(player: Player): List<Action>{
        var retval = ArrayList<Action>()
        if(player.name == "Melkar the Magnificant"){
            retval.add(Action(BakeCookies()))
            retval.add(Action(WasteTime()))
        }
        return retval
    }

    fun commitActionsForPlayer(player: Player, actions: List<Action>){
        actionsByPlayer[player] = actions
    }

    fun endTurn(){
        players.forEach{ player ->
            actionsByPlayer[player]?.forEach{
                it.type.doAction(this, player).forEach{
                    it.apply(this)
                }
            }
        }

        actionsByPlayer.clear()

        turn++
    }
    
     @Synchronized fun nextPlayerToForcast(): Player?{
         for (player in players) {
                 if (player.brain.lastCasesOfConcern == null && (player.npc)) {
                     return player
                 }
         }
        return null
    }

    fun playersAtLocation(location: Location): List<Player>{
        return players.filter { player -> player.location == location }
    }

    fun playerCharacter(): Player{
        players.forEach {
            if(!it.npc){
                return it
            }
        }
        throw Exception("No player found!")
    }
}