package shortstate.scenemaker

import shortstate.room.Room
import shortstate.ShortGameScene
import shortstate.ShortStateGame
import shortstate.ShortStateCharacter

class GoToRoomSoloMaker: SceneMaker{

    val player: ShortStateCharacter
    val room: Room

    constructor(initiator: ShortStateCharacter, room: Room){
        this.player = initiator
        this.room = room
    }

    override fun makeScene(game: ShortStateGame): ShortGameScene? {
        //TODO: check prereqs
        val sceneToMake = ShortGameScene(listOf(player), room, null)
        return sceneToMake
    }

    override fun toString(): String {
        return "Go to $room alone"
    }

    override fun cost(): Int {
        return 0
    }
}