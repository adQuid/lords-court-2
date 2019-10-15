package shortstate.scenemaker

import shortstate.room.Room
import shortstate.Scene
import shortstate.ShortStateGame
import shortstate.ShortStatePlayer

class GoToRoomSoloMaker: SceneMaker{

    val player: ShortStatePlayer
    val room: Room

    constructor(initiator: ShortStatePlayer, room: Room){
        this.player = initiator
        this.room = room
    }

    override fun makeScene(game: ShortStateGame): Scene? {
        //TODO: check prereqs
        val sceneToMake = Scene(listOf(player), room, null)
        return sceneToMake
    }

    override fun toString(): String {
        return "Go to $room alone"
    }
}