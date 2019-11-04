package aibrain

import shortstate.Scene
import shortstate.ShortStateGame
import shortstate.ShortStateCharacter
import shortstate.room.Room
import shortstate.room.action.GoToBed
import shortstate.scenemaker.ConversationMaker
import shortstate.scenemaker.GoToRoomSoloMaker
import shortstate.scenemaker.SceneMaker

class SceneBrain {

    val longBrain: ForecastBrain

    var sortedCases: List<GameCase>? = null

    constructor(longBrain: ForecastBrain){
        this.longBrain = longBrain
    }

    fun reactToScene(scene: Scene, game: ShortStateGame){
        if(scene!!.conversation != null){
            val convo = scene!!.conversation!!
            if(scene!!.conversation!!.age > 5){
                game.endScene(scene!!)
            } else {
                if(convo.otherParticipant(convo.lastSpeaker).player.npc){
                    convo.submitLine(convo.otherParticipant(convo.lastSpeaker).convoBrain.reactToLine(convo.lastLine, convo.lastSpeaker.player, game.game), game)
                }
            }
        } else {
            //TODO: Have this do other things
            if(scene!!.characters[0].player.npc){
                if(scene!!.characters[0].energy < 990){
                    GoToBed().doAction(game, scene!!.characters[0])
                } else {
                    game.endScene(scene!!)
                }
            }
        }
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