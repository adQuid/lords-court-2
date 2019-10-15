package shortstate

import action.Action
import game.Player
import shortstate.scenemaker.SceneMaker

class ShortStatePlayer {

    val player: Player
    var energy: Double
    var prospectiveActions = mutableListOf<Action>()
    var nextSceneIWannaBeIn: SceneMaker? = null

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

    fun decideNextScene(game: ShortStateGame){
        nextSceneIWannaBeIn = player.sceneBrain.nextSceneIWantToBeIn(this, game)
    }
}