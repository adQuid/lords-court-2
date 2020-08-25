package aibrain.scenecreationadvocates

import aibrain.conversationadvocates.SpecialLineAdvocate
import aibrain.scenereactionadvocates.LeaveSceneAdvocate
import aibrain.scenereactionadvocates.SceneReactionAdvocate
import aibrain.scenereactionadvocates.TalkMoreAdvocate
import game.GameCharacter
import shortstate.ShortStateCharacter
import shortstate.ShortStateGame
import shortstate.dialog.linetypes.Farewell
import shortstate.room.Room
import shortstate.scenemaker.ConversationMaker
import shortstate.scenemaker.GoToRoomSoloMaker
import shortstate.scenemaker.SceneMaker

class TalkToImportantPersonAdvocate: SceneCreationAdvocate {

    override val me: ShortStateCharacter
    private val reactionAdvocates: List<SceneReactionAdvocate>

    constructor(character: ShortStateCharacter) : super() {
        me = character
        reactionAdvocates = listOf(TalkMoreAdvocate(me.player))
    }

    override fun createScene(game: ShortStateGame, player: ShortStateCharacter): SceneMaker{
        val charactersToTalkTo = game.players.filter{it != me}
            .associate{
                ConversationMaker(me, it, roomToMeetCharacterIn(it, game)) to
                        reactionAdvocates.map { adv -> if(adv is LeaveSceneAdvocate){SceneCreationWeight(0.0,adv)} else { SceneCreationWeight(adv.weight(game, ConversationMaker(me, it, roomToMeetCharacterIn(it, game)).makeScene(game)!!),adv) }}
            .sortedByDescending { it.weight }[0]
            }

        if(charactersToTalkTo.isNotEmpty()){
            return charactersToTalkTo.maxBy { it.value.weight }!!.key
        }

        return GoToRoomSoloMaker(player, game.location.startRoom())
    }

    override fun reactionAdvocates(game: ShortStateGame): List<SceneReactionAdvocate> {
        return reactionAdvocates
    }

    private fun characterIWantToTalkTo(): GameCharacter?{
        var sortedCases = me.player.brain.lastCasesOfConcern!!.filter { it.plan.player?.location == me.player.location }

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