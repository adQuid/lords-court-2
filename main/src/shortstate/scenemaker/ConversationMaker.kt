package shortstate.scenemaker

import shortstate.*

class ConversationMaker: SceneMaker {

    val initiator: ShortStatePlayer
    val target: ShortStatePlayer
    val room: Room

    constructor(initiator: ShortStatePlayer, target: ShortStatePlayer, room: Room){
        this.initiator = initiator
        this.target = target
        this.room = room
    }

    override fun makeScene(game: ShortStateGame): Scene? {
        //TODO: make sure characters aren't already in scene
        val convo = Conversation(initiator, target)
        val sceneToMake = Scene(listOf(initiator, target), room, convo)
        return sceneToMake
    }

    override fun toString(): String {
        return "Talk to $target in $room"
    }
}