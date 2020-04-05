package aibrain.scenecreationadvocates

import game.GameCharacter
import shortstate.ShortStateCharacter
import shortstate.ShortStateGame
import shortstate.dialog.linetypes.Approve
import shortstate.dialog.linetypes.Farewell
import shortstate.room.Room
import shortstate.scenemaker.ConversationMaker
import shortstate.scenemaker.GoToRoomSoloMaker
import shortstate.scenemaker.SceneMaker

class TalkToImportantPersonAdvocate: SceneCreationAdvocate {

    private val me: ShortStateCharacter

    constructor(character: ShortStateCharacter) : super(character) {
        me = character
    }

    override fun weight(game: ShortStateGame): Double{
        if(characterIWantToTalkTo() == null){
            return 0.0
        }
        if(!(me.convoBrain.startConversation(characterIWantToTalkTo()!!, game.game) is Farewell)){
            return 100.0
        }
        return 0.0
    }

    override fun createScene(game: ShortStateGame, player: ShortStateCharacter): SceneMaker{
        val target = characterIWantToTalkTo()

        if(target != null){
            return ConversationMaker(player, game.shortPlayerForLongPlayer(target!!)!!,
                roomToMeetCharacterIn(game.shortPlayerForLongPlayer(target!!)!!, game))
        }

        return GoToRoomSoloMaker(player, game.location.startRoom())
    }

    private fun characterIWantToTalkTo(): GameCharacter?{
        var sortedCases = me.player.brain.lastCasesOfConcern

        while(sortedCases != null && sortedCases!!.isNotEmpty()){
            val hopefulCase = sortedCases!![0]
            sortedCases = sortedCases!!.subList(1,sortedCases!!.size)
            if(hopefulCase.plan.player != me.player){
                return hopefulCase.plan.player
            }
        }
        return null
    }

    private fun roomToMeetCharacterIn(target: ShortStateCharacter, game: ShortStateGame): Room {

        if(game.location.hasType(Room.RoomType.ETC)){
            return game.location.roomByType(Room.RoomType.ETC)
        }

        return game.location.startRoom()
    }
}