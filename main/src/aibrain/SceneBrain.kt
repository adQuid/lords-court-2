package aibrain

import shortstate.ShortStateGame
import shortstate.ShortStateCharacter
import shortstate.room.Room
import shortstate.scenemaker.ConversationMaker
import shortstate.scenemaker.GoToRoomSoloMaker
import shortstate.scenemaker.SceneMaker

class SceneBrain {

    val longBrain: ForecastBrain

    var sortedCases: List<GameCase>? = null

    constructor(longBrain: ForecastBrain){
        this.longBrain = longBrain
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
        game.location.rooms.forEach {
            if(it.type == Room.RoomType.ETC){
                return it
            }
        }
        return game.location.startRoom()
    }
}