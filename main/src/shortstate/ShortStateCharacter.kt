package shortstate

import game.action.Action
import aibrain.ConversationBrain
import aibrain.SceneBrain
import game.Character
import shortstate.report.Report
import shortstate.scenemaker.SceneMaker

class ShortStateCharacter {

    //testing only
    val id = Math.random()

    val player: Character
    var sceneBrain: SceneBrain
    var convoBrain: ConversationBrain
    var energy: Int
    var knownReports = mutableListOf<Report>()
    var prospectiveActions = mutableListOf<Action>()
    var nextSceneIWannaBeIn: SceneMaker? = null

    constructor(player: Character){
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