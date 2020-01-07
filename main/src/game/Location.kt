package game

import shortstate.room.Room

class Location {

    val id: Int
    var rooms: List<Room>

    constructor(game: Game){
        id = game.nextID++
        rooms = listOf(
            Room("Bedchamber", "assets//rooms//bedroom.png", Room.RoomType.BEDROOM),
            Room("Hallway", "assets//rooms//testRoom.png", Room.RoomType.ETC),
            Room("Workroom", "assets//rooms//workRoom.png", Room.RoomType.WORKROOM)
        )
    }

    constructor(other: Location){
        this.id = other.id
        this.rooms = other.rooms.map { room -> room }
    }

    constructor(saveString: Map<String, Any>, game:Game): this(game)

    fun saveString(): Map<String, Any>{
        val retval = mutableMapOf<String, Any>()

        //TODO: As locations get more complicated, make this more dynamic

        return retval
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