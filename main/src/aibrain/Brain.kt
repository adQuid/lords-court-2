package aibrain

import actions.BakeCookies
import game.Action
import game.Game
import game.Player
import util.safeSublist
import java.math.BigDecimal

class Brain {
    var player: Player

    var maxPlayersToThinkAbout = 3
    var maxPlansToThinkAbout = 9

    constructor(player: Player){
        this.player = player
    }

    fun determineIdealPlan(game: Game): Plan{
        val topPlayers = safeSublist(mostSignificantPlayersToMe(game),0,maxPlayersToThinkAbout)

        var cases = mutableListOf<GameCase>()

        topPlayers.forEach{player ->
            possibleActionsForPlayer(game, player).forEach{actions ->
               cases.add(GameCase(game, actions))
            }
        }

        cases.sortBy { case -> -case.probability }
        var importantCases = safeSublist(cases,0, maxPlansToThinkAbout)



        return Plan(ArrayList<Map<Player,Action>>())
    }

    fun mostSignificantPlayersToMe(game: Game): List<Player>{
        var retval = mutableListOf<Player>()
        game.players.forEach{
            if(!it.equals(player)){
                retval.add(it)
            }
        }

        return retval
    }

    private fun possibleActionsForPlayer(game: Game, player: Player): List<PotentialActions>{
        var retval = ArrayList<PotentialActions>()
        retval.add(PotentialActions(player, listOf<Action>(), 0.5))
        if(player.name == "player"){
            retval.add(PotentialActions(player,listOf(Action(BakeCookies())), 0.5))
        }
        return retval
    }

    private fun supposeCase(gameCase: GameCase): Double{
        gameCase.game.endTurn()
        return evaluateGame(gameCase.game) * gameCase.probability
    }

    private fun evaluateGame(game: Game): Double {
        return game.deliciousness * 1.0
    }
}