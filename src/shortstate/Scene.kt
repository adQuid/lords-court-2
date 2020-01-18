package shortstate

import game.Character
import shortstate.room.Room

class Scene {

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

    fun saveString(): Map<String, Any>{
        return hashMapOf(
            CONVERSATION_NAME to {conversation!!.saveString(); if(conversation != null) else null },
            CHARACTERS_NAME to characters.map { character -> character.saveString() },
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

    fun shortPlayerForLongPlayer(player: Character): ShortStateCharacter?{
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