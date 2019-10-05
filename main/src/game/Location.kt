package game

import shortstate.Room

class Location {

    var rooms: List<Room>

    constructor(){
        rooms = listOf(Room("assets//rooms//bedroom.png"), Room("assets//rooms//testRoom.png"))
    }

    fun startRoom(): Room{
        return rooms[0]
    }
}