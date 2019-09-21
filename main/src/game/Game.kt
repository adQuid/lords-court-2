package game

import action.Action

class Game {
    var turn = 1
    var players = mutableListOf<Player>()
    var actionsByPlayer = HashMap<Player, List<Action>>()

    //temporary stat
    var deliciousness = 0

    constructor(){
        players.add(Player("Melkar the Magnificant", false))
        players.add(Player("npc", true))
        
    }

    constructor(other: Game){
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

    fun commitActionsForPlayer(player: Player, actions: List<Action>){
        actionsByPlayer[player] = actions
        if(actionsByPlayer.keys.size == players.size){
            endTurn()
        }
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
    }
    
     @Synchronized fun nextPlayerWithoutActions(): Player?{
         for (player in players) {
                 if (actionsByPlayer[player] == null && (player.npc)) {
                     return player
                 }
         }

        return null
    }
}