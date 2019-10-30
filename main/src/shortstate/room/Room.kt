package shortstate.room

import shortstate.room.action.CommitToCookies
import shortstate.room.action.GoToBed
import shortstate.room.action.ReportOnDeliciousness

class Room {

    val name: String
    val type: RoomType
    val pictureText: String
    val actions: List<RoomAction>

    constructor(name: String, pictureText: String, type: RoomType){
        this.name = name
        this.type = type
        this.pictureText = pictureText
        if(type == RoomType.BEDROOM){
            actions = listOf(GoToBed())
        } else if(type == RoomType.WORKROOM){
            actions = listOf(CommitToCookies(), ReportOnDeliciousness())
        } else {
            actions = listOf()
        }
    }

    override fun toString(): String {
        return name
    }

    enum class RoomType{
        BEDROOM, WORKROOM, ETC
    }

}