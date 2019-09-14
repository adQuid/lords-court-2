package game

import java.util.function.Consumer

class Game {
    var turn = 1
    var players = mutableListOf<Player>()
    var actionsByPlayer = HashMap<Player, List<Action>>()

    //temporary stat
    var deliciousness = 0

    constructor(){
        players.add(Player("player"))
        players.add(Player("npc"))

        players.forEach{
            actionsByPlayer[it] = listOf()
        }
    }

    constructor(other: Game){
        other.players.forEach{
            this.players.add(Player(it))
        }
    }

    fun image(): Game{
        var retval = Game(this)

        players.forEach{
            actionsByPlayer[it] = listOf()
        }
        return retval
    }

    fun endTurn(){
        players.forEach{ player ->
            actionsByPlayer[player]?.forEach{
                it.type.doAction(this, player)
            }
        }
    }
}