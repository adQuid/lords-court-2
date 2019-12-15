package aibrain.scenecreationadvocates

import game.Character
import shortstate.ShortStateGame
import shortstate.room.Room
import shortstate.scenemaker.GoToRoomSoloMaker
import shortstate.scenemaker.SceneMaker

class GoToWorkroomAdvocate: SceneCreationAdvocate {

    private val me: Character

    constructor(character: game.Character) : super(character) {
        me = character
    }

    override fun weight(game: ShortStateGame): Double{
        return 15.0 - (15.0 * game.shortPlayerForLongPlayer(me)!!.knownReports.size)
    }

    override fun createScene(game: ShortStateGame, player: shortstate.ShortStateCharacter): SceneMaker{
        return GoToRoomSoloMaker(player, game.location.roomByType(Room.RoomType.WORKROOM))
    }
}