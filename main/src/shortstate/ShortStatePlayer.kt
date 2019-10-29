package shortstate

import action.Action
import aibrain.ConversationBrain
import aibrain.SceneBrain
import game.Player
import shortstate.report.Report
import shortstate.scenemaker.SceneMaker
import java.math.BigDecimal

class ShortStatePlayer {

    //testing only
    val id = Math.random()

    val player: Player
    var sceneBrain: SceneBrain
    var convoBrain: ConversationBrain
    var energy: Int
    var knownReports = mutableListOf<Report>()
    var prospectiveActions = mutableListOf<Action>()
    var nextSceneIWannaBeIn: SceneMaker? = null

    constructor(player: Player){
        this.player = player
        this.energy = 1000
        sceneBrain = SceneBrain(player.brain)
        convoBrain = ConversationBrain(player.brain)
    }

    override fun toString(): String {
        return player.toString() + id.toString()
    }

    fun addEnergy(amount: Int): Boolean {
        if(energy + amount >= 0) {
            energy += amount
            return true
        }
        return false
    }

    fun decideNextScene(game: ShortStateGame){
        nextSceneIWannaBeIn = sceneBrain.nextSceneIWantToBeIn(this, game)
    }
}