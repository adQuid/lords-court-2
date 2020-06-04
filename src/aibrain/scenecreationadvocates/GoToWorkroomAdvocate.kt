package aibrain.scenecreationadvocates

import aibrain.scenereactionadvocates.DraftWritAdvocate
import aibrain.scenereactionadvocates.GetReportAdvocate
import aibrain.scenereactionadvocates.SceneReactionAdvocate
import shortstate.ShortStateCharacter
import shortstate.ShortStateGame
import shortstate.room.Room
import shortstate.scenemaker.GoToRoomSoloMaker
import shortstate.scenemaker.SceneMaker

class GoToWorkroomAdvocate: SceneCreationAdvocate {

    override val me: ShortStateCharacter
    override val reactionAdvocates: List<SceneReactionAdvocate>

    constructor(character: ShortStateCharacter) : super() {
        me = character
        reactionAdvocates = listOf(GetReportAdvocate(me.player), DraftWritAdvocate(me.player))
    }

    override fun createScene(game: ShortStateGame, player: shortstate.ShortStateCharacter): SceneMaker{
        return GoToRoomSoloMaker(player, game.location.roomByType(Room.RoomType.WORKROOM))
    }
}