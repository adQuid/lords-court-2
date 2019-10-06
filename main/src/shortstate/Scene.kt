package shortstate

class Scene {

    val conversation: Conversation?
    val characters: List<ShortStatePlayer>
    val room: Room

    constructor(characters: List<ShortStatePlayer>, room: Room, conversation: Conversation?){
        this.characters = characters
        this.room = room
        this.conversation = conversation
    }

}