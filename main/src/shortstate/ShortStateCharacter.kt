package shortstate

import game.action.Action
import aibrain.ConversationBrain
import aibrain.SceneBrain
import game.Character
import shortstate.dialog.Line
import shortstate.dialog.linetypes.Announcement
import shortstate.dialog.linetypes.RequestReport
import shortstate.report.Report
import shortstate.report.ReportType
import shortstate.scenemaker.SceneMaker

class ShortStateCharacter {

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
        convoBrain = ConversationBrain(this)
    }

    override fun toString(): String {
        return player.toString()
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

    fun defaultConversationLines(): List<Line>{
        return listOf(
            Announcement(null),
            RequestReport(null)
        )
    }

    fun reportOfType(type: ReportType): Report {
        return knownReports.filter { report -> report.type() == type }[0]
    }
}