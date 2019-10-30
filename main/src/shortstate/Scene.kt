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

}