package shortstate.report

import game.Game

class DeliciousnessReport: Report{
    
    val value: Int
    
    constructor(deliciousness: Int){
        value = deliciousness
    }
    
    override fun apply(game: Game) {
       game.deliciousness = value
    }

    override fun toString(): String {
        return "Game has $value Deliciousness"
    }
}