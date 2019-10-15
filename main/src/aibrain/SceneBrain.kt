package aibrain

import shortstate.ShortStateGame
import shortstate.ShortStatePlayer
import shortstate.scenemaker.GoToRoomSoloMaker
import shortstate.scenemaker.SceneMaker

class SceneBrain {

    val longBrain: ForecastBrain

    constructor(longBrain: ForecastBrain){
        this.longBrain = longBrain
    }

    fun nextSceneIWantToBeIn(player: ShortStatePlayer, game: ShortStateGame): SceneMaker?{
        return GoToRoomSoloMaker(player, game.location.startRoom())
    }
}