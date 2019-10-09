package shortstate

import game.Player

class ShortStatePlayer {

    val player: Player
    var energy: Double

    constructor(player: Player){
        this.player = player
        this.energy = 10.0
    }

    override fun toString(): String {
        return player.toString()
    }

    fun addEnergy(amount: Double): Boolean {
        if(energy + amount >= 0) {
            energy += amount
            return true
        }
        return false
    }
}