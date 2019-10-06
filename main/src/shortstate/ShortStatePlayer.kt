package shortstate

import game.Player

class ShortStatePlayer {

    val player: Player
    var energy: Int

    constructor(player: Player){
        this.player = player
        this.energy = 10
    }

    override fun toString(): String {
        return player.toString()
    }
}