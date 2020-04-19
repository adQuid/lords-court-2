package aibrain.scenecreationadvocates

import shortstate.ShortStateCharacter
import shortstate.ShortStateGame
import shortstate.room.Room
import shortstate.scenemaker.GoToRoomSoloMaker
import shortstate.scenemaker.SceneMaker

class GoToWorkroomAdvocate: SceneCreationAdvocate {

    private val me: ShortStateCharacter

    constructor(character: ShortStateCharacter) : super(character) {
        me = character
    }

    override fun weight(game: ShortStateGame): Double{
        return 15.0 - (15.0 * me.knownReports.size)
    }

    override fun createScene(game: ShortStateGame, player: shortstate.ShortStateCharacter): SceneMaker{
        return GoToRoomSoloMaker(player, game.location.roomByType(Room.RoomType.WORKROOM))
    }
}