package aibrain.scenecreationadvocates

import game.Character
import shortstate.Scene
import shortstate.ShortStateCharacter
import shortstate.ShortStateGame
import shortstate.room.Room
import shortstate.scenemaker.ConversationMaker
import shortstate.scenemaker.GoToRoomSoloMaker
import shortstate.scenemaker.SceneMaker

class TalkToImportantPersonAdvocate: SceneCreationAdvocate {

    private val me: Character

    constructor(character: game.Character) : super(character) {
        me = character
    }

    override fun weight(game: ShortStateGame): Double{
        if(me.brain.lastCasesOfConcern != null && me.brain.lastCasesOfConcern!!.isNotEmpty()){
            return 10.0
        }
        return 0.0
    }

    override fun createScene(game: ShortStateGame, player: shortstate.ShortStateCharacter): SceneMaker{
        var sortedCases = player.player.brain.lastCasesOfConcern

        while(sortedCases != null && sortedCases!!.isNotEmpty()){
            val hopefulCase = sortedCases!![0]
            sortedCases = sortedCases!!.filter { it.plan.player != hopefulCase.plan.player }
            if(hopefulCase.plan.player != player.player && game.shortPlayerForLongPlayer(hopefulCase.plan.player) != null){
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