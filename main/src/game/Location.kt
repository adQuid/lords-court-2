package game

import shortstate.Room

class Location {

    var rooms: List<Room>

    constructor(){
        rooms = listOf(Room("assets//rooms//testRoom.png"), Room("assets//rooms//bedroom.png"))
    }

    fun startRoom(): Room{
        return rooms[1]
    }
}