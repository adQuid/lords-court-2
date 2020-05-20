package game

import shortstate.room.Room

class Location {

    val id: Int
    var rooms: List<Room> = listOf(
        Room("Bedchamber", "assets/rooms/bedroom", Room.RoomType.BEDROOM),
        Room("Hallway", "assets/rooms/testRoom", Room.RoomType.ETC),
        Room("Workroom", "assets/rooms/workRoom", Room.RoomType.WORKROOM),
        Room("Throne Room", "assets/rooms/throneRoom", Room.RoomType.THRONEROOM)
    )

    constructor(game: Game){
        id = game.nextID++
    }

    constructor(other: Location){
        this.id = other.id
        this.rooms = other.rooms.map { room -> room }
    }

    constructor(parent: Game, saveString: Map<String, Any>){
        this.id = saveString["ID"] as Int
    }

    fun saveString(): Map<String, Any>{
        return mutableMapOf<String, Any>(
            "ID" to id
        )
    }

    override fun equals(other: Any?): Boolean {
        if(other is Location){
            return this.id == other.id
        } else {
            return false
        }
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