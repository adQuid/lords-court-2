package shortstate

import game.GameCharacter
import shortstate.room.Room

class ShortGameScene {

    val CONVERSATION_NAME = "CONVO"
    val conversation: Conversation?
    val CHARACTERS_NAME = "CHARACTERS"
    val characters: List<ShortStateCharacter>
    val ROOM_NAME = "ROOM"
    val room: Room

    constructor(characters: List<ShortStateCharacter>, room: Room, conversation: Conversation?){
        this.characters = characters
        this.room = room
        this.conversation = conversation
    }

    constructor(parent: ShortStateGame, saveString: Map<String,Any?>){
        if(saveString[CONVERSATION_NAME] == null){
            conversation = null
        } else {
            conversation = Conversation(parent, saveString[CONVERSATION_NAME] as Map<String, Any>)
        }
        this.characters = (saveString[CHARACTERS_NAME] as List<Int>).map { id -> parent.shortPlayerForLongPlayer(parent.game.characterById(id))!! }
        this.room = Room(parent, saveString[ROOM_NAME] as Map<String, Any>)
    }

    fun saveString(): Map<String, Any?>{
        return mapOf(
            CONVERSATION_NAME to {if(conversation != null){conversation!!.saveString()} else{ null }}(),
            CHARACTERS_NAME to characters.map { character -> character.player.id },
            ROOM_NAME to room.saveString()
        )
    }

    fun nextPlayerToDoSomething(): ShortStateCharacter{
        if(conversation!= null) {
            return conversation.otherParticipant(conversation.lastSpeaker)
        } else {
            return characters[0]
        }
    }

    fun shortPlayerForLongPlayer(player: GameCharacter): ShortStateCharacter?{
        characters.forEach {
            if(it.player == player){
                return it
            }
        }
        return null
    }

    fun hasAPC(): Boolean{
        return characters.filter { char -> !char.player.npc }.isNotEmpty()
    }
}