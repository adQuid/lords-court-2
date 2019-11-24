package shortstate

import shortstate.room.Room

class Scene {

    val conversation: Conversation?
    val characters: List<ShortStateCharacter>
    val room: Room

    constructor(characters: List<ShortStateCharacter>, room: Room, conversation: Conversation?){
        this.characters = characters
        this.room = room
        this.conversation = conversation
    }

    fun nextPlayerToDoSomething(): ShortStateCharacter{
        if(conversation!= null) {
            return conversation.otherParticipant(conversation.lastSpeaker)
        } else {
            return characters[0]
        }
    }

    fun hasAPC(): Boolean{
        return characters.filter { char -> !char.player.npc }.isNotEmpty()
    }
}