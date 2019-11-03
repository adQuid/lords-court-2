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

    constructor(other: Location){
        this.rooms = other.rooms.map { room -> room }
    }

    fun startRoom(): Room {
        return rooms[0]
    }

    fun hasType(type: Room.RoomType): Boolean{
        return roomByType(type) != null
    }

    fun roomByType(type: Room.RoomType): Room{
        rooms.forEach {
            if(it.type == type){
                return it
            }
        }
        throw Exception("Room type $type does not exist!")
    }
}