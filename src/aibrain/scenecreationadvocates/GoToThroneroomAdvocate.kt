package aibrain.scenecreationadvocates

import aibrain.scenereactionadvocates.EnactWritAdvocate
import aibrain.scenereactionadvocates.GoToBedAdvocate
import aibrain.scenereactionadvocates.SceneReactionAdvocate
import shortstate.ShortStateCharacter
import shortstate.ShortStateGame
import shortstate.room.Room
import shortstate.scenemaker.GoToRoomSoloMaker
import shortstate.scenemaker.SceneMaker

class GoToThroneroomAdvocate: SceneCreationAdvocate {

    override val me: ShortStateCharacter
    override val reactionAdvocates: List<SceneReactionAdvocate>

    constructor(character: ShortStateCharacter) : super(character) {
        me = character
        reactionAdvocates = listOf(EnactWritAdvocate(me.player))
    }

    override fun createScene(game: ShortStateGame, player: shortstate.ShortStateCharacter): SceneMaker{
        return GoToRoomSoloMaker(player, game.location.roomByType(Room.RoomType.THRONEROOM))
    }
}