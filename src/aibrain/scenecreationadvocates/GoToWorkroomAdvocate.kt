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

    constructor(character: ShortStateCharacter) : super() {
        me = character
    }

    override fun createScene(game: ShortStateGame, player: shortstate.ShortStateCharacter): SceneMaker{
        return GoToRoomSoloMaker(player, game.location.roomByType(Room.RoomType.WORKROOM))
    }

    override fun reactionAdvocates(game: ShortStateGame): List<SceneReactionAdvocate> {
        return listOf(GetReportAdvocate(game.game.characterById(me.player.id)), DraftWritAdvocate(game.game.characterById(me.player.id)))
    }
}