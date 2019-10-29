package aibrain

import shortstate.ShortStateGame
import shortstate.ShortStatePlayer
import shortstate.scenemaker.ConversationMaker
import shortstate.scenemaker.GoToRoomSoloMaker
import shortstate.scenemaker.SceneMaker

class SceneBrain {

    val longBrain: ForecastBrain

    constructor(longBrain: ForecastBrain){
        this.longBrain = longBrain
    }

    fun nextSceneIWantToBeIn(player: ShortStatePlayer, game: ShortStateGame): SceneMaker?{

        val sortedCases = longBrain.lastCasesOfConcern!!.toList().sortedBy { entry -> entry.second }.map { entry -> entry.first }

        //if someone else did something for the best case, go ask them about it
        if(sortedCases[0].plan.player != longBrain.player && game.shortPlayerForLongPlayer(sortedCases[0].plan.player) != null){
            return ConversationMaker(player, game.shortPlayerForLongPlayer(sortedCases[0].plan.player)!!, game.location.startRoom())
        }

        return GoToRoomSoloMaker(player, game.location.startRoom())
    }
}