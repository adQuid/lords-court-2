package shortstate.scenemaker

import shortstate.*
import shortstate.room.Room

class ConversationMaker: SceneMaker {

    val initiator: ShortStateCharacter
    val target: ShortStateCharacter
    val room: Room

    val energyCost = 150

    constructor(initiator: ShortStateCharacter, target: ShortStateCharacter, room: Room){
        this.initiator = initiator
        this.target = target
        this.room = room
    }

    override fun makeScene(game: ShortStateGame): ShortGameScene? {
        //TODO: make sure characters aren't already in scene
        if(initiator.addEnergy(-energyCost)){
            val convo = Conversation(room, initiator, target)
            val sceneToMake = ShortGameScene(listOf(initiator, target), room, convo)
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