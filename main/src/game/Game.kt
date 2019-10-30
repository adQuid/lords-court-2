package game

import game.action.Action
import game.action.actionTypes.BakeCookies
import game.action.actionTypes.WasteTime
import game.titlemaker.TitleFactory

class Game {
    var isLive = true
    var turn = 1
    var players = mutableListOf<Character>()
    var locations = listOf(Location())
    var actionsByPlayer = HashMap<Character, List<Action>>()

    //temporary stat
    var deliciousness = 0

    constructor(){
        val PC = Character("Melkar the Magnificant", false, locations[0])
        PC.titles.add(TitleFactory.makeCountTitle("Cookies"))
        players.add(PC)

        players.add(Character("npc", true, locations[0]))
        
    }

    constructor(other: Game){
        isLive = false
        other.players.forEach{
            this.players.add(Character(it))
        }
    }

    fun imageFor(player: Character): Game{
        var retval = Game(this)

        players.forEach{
            retval.actionsByPlayer[it] = listOf()
        }
        return retval
    }

    fun possibleActionsForPlayer(player: Character): List<Action>{
        var retval = ArrayList<Action>()
        if(player.name == "Melkar the Magnificant"){
            retval.add(Action(BakeCookies()))
            retval.add(Action(WasteTime()))
        }
        return retval
    }

    fun commitActionsForPlayer(player: Character, actions: List<Action>){
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
}