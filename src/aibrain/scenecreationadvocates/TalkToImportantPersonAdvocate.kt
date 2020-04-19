package aibrain.scenecreationadvocates

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

    private val me: ShortStateCharacter

    private val reactionAdvocates: List<SceneReactionAdvocate>

    constructor(character: ShortStateCharacter) : super(character) {
        me = character
        reactionAdvocates = listOf(TalkMoreAdvocate(me.player))
    }

    override fun weight(game: ShortStateGame): Double{
        val prospectiveScene = createScene(game, me).makeScene(game)
        if(prospectiveScene != null){
            return reactionAdvocates.map { adv -> adv.weight(game, prospectiveScene) }.sortedByDescending { it }[0]
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