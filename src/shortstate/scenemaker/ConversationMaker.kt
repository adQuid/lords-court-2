package shortstate.scenemaker

import shortstate.*
import shortstate.dialog.Line
import shortstate.room.Room

class ConversationMaker: SceneMaker {

    val initiator: ShortStateCharacter
    val target: ShortStateCharacter
    val room: Room
    val firstLine: Line?

    constructor(initiator: ShortStateCharacter, target: ShortStateCharacter, room: Room, firstLine: Line? = null){
        this.initiator = initiator
        this.target = target
        this.room = room
        this.firstLine = firstLine
    }

    override fun makeScene(game: ShortStateGame): ShortGameScene? {
        val convo = Conversation(game, room, initiator, target)
        if(firstLine != null){
            convo.submitLine(firstLine, game)
        }
        val sceneToMake = ShortGameScene(listOf(initiator, target), room, convo)
        return sceneToMake
    }

    override fun toString(): String {
        return "Talk to $target"
    }
}