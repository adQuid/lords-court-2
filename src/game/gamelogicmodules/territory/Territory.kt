package game.gamelogicmodules.territory

import game.Title

class Territory {

    var wheat: Int
    var flour: Int
    var bread: Int

    constructor(){
        wheat = 1
        flour = 2
        bread = 3
    }

    constructor(other: Territory){
        wheat = other.wheat
        flour = other.flour
        bread = other.bread
    }

    fun generateCountTitle(): Title{
        return Count("place")
    }
}