package aibrain.scenecreationadvocates

import aibrain.scenereactionadvocates.EnactWritAdvocate
import aibrain.scenereactionadvocates.GoToBedAdvocate
import aibrain.scenereactionadvocates.SceneReactionAdvocate
import game.GameCharacter
import shortstate.ShortStateCharacter
import shortstate.ShortStateGame
import shortstate.room.Room
import shortstate.scenemaker.GoToRoomSoloMaker
import shortstate.scenemaker.SceneMaker

class GoToThroneroomAdvocate: SceneCreationAdvocate {

    override val me: ShortStateCharacter

    constructor(character: ShortStateCharacter) : super() {
        me = character
    }

    override fun createScene(game: ShortStateGame, player: shortstate.ShortStateCharacter): SceneMaker{
        return GoToRoomSoloMaker(player, game.location.roomByType(Room.RoomType.THRONEROOM))
    }

    override fun reactionAdvocates(game: ShortStateGame): List<SceneReactionAdvocate>{
        return listOf(EnactWritAdvocate(GameCharacter(me.player, game.game)))
    }
}