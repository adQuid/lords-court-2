package shortstate.scenemaker

import shortstate.*
import shortstate.room.Room

class ConversationMaker: SceneMaker {

    val initiator: ShortStatePlayer
    val target: ShortStatePlayer
    val room: Room

    val energyCost = 1.5

    constructor(initiator: ShortStatePlayer, target: ShortStatePlayer, room: Room){
        this.initiator = initiator
        this.target = target
        this.room = room
    }

    override fun makeScene(game: ShortStateGame): Scene? {
        //TODO: make sure characters aren't already in scene
        if(initiator.addEnergy(-energyCost)){
            val convo = Conversation(initiator, target)
            val sceneToMake = Scene(listOf(initiator, target), room, convo)
            return sceneToMake
        }
        return null
    }

    override fun toString(): String {
        if(initiator.energy >= energyCost){
            return "Talk to $target in $room"
        } else {
            return "Talk to $target in $room (NOT ENOUGH ENERGY)"
        }
    }
}