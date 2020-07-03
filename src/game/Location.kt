package game

import shortstate.room.Room

class Location {

    companion object{
        var nextId = 0
    }

    val id: Int
    val x: Int
    val y: Int
    var rooms: List<Room> = listOf(
        Room("Bedchamber", "assets/rooms/bedroom", Room.RoomType.BEDROOM),
        Room("Hallway", "assets/rooms/testRoom", Room.RoomType.ETC),
        Room("Workroom", "assets/rooms/workRoom", Room.RoomType.WORKROOM),
        Room("Throne Room", "assets/rooms/throneRoom", Room.RoomType.THRONEROOM)
    )

    constructor(x: Int, y:Int){
        id = nextId++
        this.x = x
        this.y = y
    }

    constructor(other: Location){
        this.id = other.id
        this.x = other.x
        this.y = other.y
        this.rooms = other.rooms.map { room -> room }
    }

    constructor(saveString: Map<String, Any>){
        this.id = saveString["ID"] as Int
        if(this.id > nextId){
            nextId = this.id
        }
        this.x = saveString["X"] as Int
        this.y = saveString["Y"] as Int
    }

    fun saveString(): Map<String, Any>{
        return mutableMapOf<String, Any>(
            "ID" to id,
            "X" to x,
            "Y" to y
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