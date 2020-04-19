package shortstate.scenemaker

import shortstate.*
import shortstate.room.Room

class ConversationMaker: SceneMaker {

    val initiator: ShortStateCharacter
    val target: ShortStateCharacter
    val room: Room

    constructor(initiator: ShortStateCharacter, target: ShortStateCharacter, room: Room){
        this.initiator = initiator
        this.target = target
        this.room = room
    }

    override fun makeScene(game: ShortStateGame): ShortGameScene? {
        val convo = Conversation(room, initiator, target)
        val sceneToMake = ShortGameScene(listOf(initiator, target), room, convo)
        return sceneToMake
    }

    override fun toString(): String {
        return "Talk to $target in $room"
    }
}