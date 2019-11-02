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

    //This is more of a "secondary" pointer, and is expected to be duplicated with anyone actually holding the titles.
    //These exist to prevent titles from vanishing when nobody is holding them
    val titles = mutableSetOf<Title>()

    //temporary stat
    var deliciousness = 0

    constructor(){
        val PC = Character("Melkar the Magnificant", "assets//general//conversation frame.png", false, locations[0])
        applyTitleToCharacter(TitleFactory.makeCountTitle("Cookies"), PC)
        players.add(PC)

        val NPC = Character("Frip", "assets//portraits//faceman.png", true, locations[0])
        NPC.titles.add(TitleFactory.makeCountTitle("Eating Cookies"))
        NPC.titles.add(TitleFactory.makeCountTitle("Nomz, munch, and food-related goodness"))
        players.add(NPC)

        val NPC2 = Character("Omrin", "assets//portraits//stacheman.png", true, locations[0])
        players.add(NPC2)
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

    fun applyTitleToCharacter(title: Title, character: Character){
        titles.add(title)
        character.titles.add(title)
    }
}