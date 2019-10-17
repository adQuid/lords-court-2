package shortstate.room

import shortstate.room.action.CommitToCookies
import shortstate.room.action.GoToBed

class Room {

    val name: String
    val pictureText: String
    val actions: List<RoomAction>

    constructor(name: String, pictureText: String, type: RoomType){
        this.name = name
        this.pictureText = pictureText
        if(type == RoomType.BEDROOM){
            actions = listOf(GoToBed(), CommitToCookies())
        } else {
            actions = listOf()
        }
    }

    override fun toString(): String {
        return name
    }

    enum class RoomType{
        BEDROOM, ETC
    }

}