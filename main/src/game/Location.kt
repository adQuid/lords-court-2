package game

import shortstate.room.Room

class Location {

    var rooms: List<Room>

    constructor(){
        rooms = listOf(
            Room("Bedchamber", "assets//rooms//bedroom.png", Room.RoomType.BEDROOM),
            Room("Hallway", "assets//rooms//testRoom.png", Room.RoomType.ETC),
            Room("Workroom", "assets//rooms//workRoom.png", Room.RoomType.WORKROOM)
        )
    }

    fun startRoom(): Room {
        return rooms[0]
    }
}