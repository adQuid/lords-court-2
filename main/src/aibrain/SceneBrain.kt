package aibrain

import aibrain.scenereactionadvocates.GoToBedAdvocate
import aibrain.scenereactionadvocates.LeaveSceneAdvocate
import aibrain.scenereactionadvocates.SceneReactionAdvocate
import aibrain.scenereactionadvocates.TalkMoreAdvocate
import shortstate.Scene
import shortstate.ShortStateGame
import shortstate.ShortStateCharacter
import shortstate.room.Room
import shortstate.scenemaker.ConversationMaker
import shortstate.scenemaker.GoToRoomSoloMaker
import shortstate.scenemaker.SceneMaker

class SceneBrain {

    val longBrain: ForecastBrain

    var sortedCases: List<GameCase>? = null

    val advocates: List<SceneReactionAdvocate>

    constructor(longBrain: ForecastBrain){
        this.longBrain = longBrain
        advocates = listOf(TalkMoreAdvocate(longBrain.player), GoToBedAdvocate(longBrain.player), LeaveSceneAdvocate(longBrain.player))
    }

    fun reactToScene(scene: Scene, game: ShortStateGame){
        advocates.sortedByDescending { adv -> adv.weight(scene) }[0].doToScene(game, scene)
    }

    fun nextSceneIWantToBeIn(player: ShortStateCharacter, game: ShortStateGame): SceneMaker?{

        if(sortedCases == null){
            sortedCases = longBrain.sortedCases
        }

        //if someone else did something for the best case, go ask them about it
        while(sortedCases != null && sortedCases!!.isNotEmpty()){
            val hopefulCase = sortedCases!![0]
            sortedCases = sortedCases!!.filter { it.plan.player != hopefulCase.plan.player }
            if(hopefulCase.plan.player != longBrain.player && game.shortPlayerForLongPlayer(hopefulCase.plan.player) != null){
                return ConversationMaker(player, game.shortPlayerForLongPlayer(hopefulCase.plan.player)!!,
                    roomToMeetCharacterIn(game.shortPlayerForLongPlayer(hopefulCase.plan.player)!!, game))
            }
        }

        return GoToRoomSoloMaker(player, game.location.startRoom())
    }

    private fun roomToMeetCharacterIn(target: ShortStateCharacter, game: ShortStateGame): Room {

        if(game.location.hasType(Room.RoomType.ETC)){
            return game.location.roomByType(Room.RoomType.ETC)
        }

        return game.location.startRoom()
    }
}