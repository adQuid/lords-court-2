package aibrain

import game.Game
import shortstate.ShortStateGame
import shortstate.ShortStatePlayer
import shortstate.scenemaker.ConversationMaker
import shortstate.scenemaker.GoToRoomSoloMaker
import shortstate.scenemaker.SceneMaker

class SceneBrain {

    val longBrain: ForecastBrain

    var sortedCases: List<GameCase>? = null

    constructor(longBrain: ForecastBrain){
        this.longBrain = longBrain
    }

    fun nextSceneIWantToBeIn(player: ShortStatePlayer, game: ShortStateGame): SceneMaker?{

        if(sortedCases == null){
            sortedCases = longBrain.sortedCases
        }

        //if someone else did something for the best case, go ask them about it
        while(sortedCases != null && sortedCases!!.isNotEmpty()){
            val hopefulCase = sortedCases!![0]
            sortedCases = sortedCases!!.filter { it.plan.player != hopefulCase.plan.player }
            if(hopefulCase.plan.player != longBrain.player && game.shortPlayerForLongPlayer(hopefulCase.plan.player) != null){
                return ConversationMaker(player, game.shortPlayerForLongPlayer(hopefulCase.plan.player)!!, game.location.startRoom())
            }
        }

        return GoToRoomSoloMaker(player, game.location.startRoom())
    }
}