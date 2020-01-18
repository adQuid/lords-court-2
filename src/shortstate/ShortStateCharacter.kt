package shortstate

import game.action.Action
import aibrain.ConversationBrain
import aibrain.SceneBrain
import game.Character
import game.Game
import shortstate.dialog.Line
import shortstate.dialog.linetypes.Announcement
import shortstate.dialog.linetypes.RequestReport
import shortstate.report.GlobalReportTypeFactory
import shortstate.report.Report
import shortstate.report.ReportType
import shortstate.scenemaker.SceneMaker

class ShortStateCharacter {

    val PLAYER_NAME = "PLAYER"
    val player: Character
    var sceneBrain: SceneBrain
    var convoBrain: ConversationBrain
    val ENERGY_NAME = "ENERGY"
    var energy: Int
    val REPORTS_NAME = "REPORTS"
    var knownReports = mutableListOf<Report>()

    val ACTIONS_NAME = "ACTIONS"
    var prospectiveActions = mutableListOf<Action>()
    var nextSceneIWannaBeIn: SceneMaker? = null

    constructor(player: Character){
        this.player = player
        this.energy = 1000
        sceneBrain = SceneBrain(player.brain)
        convoBrain = ConversationBrain(this)
    }

    constructor(parent: Game, saveString: Map<String, Any>){
        this.player = parent.matchingPlayer(parent.characterById(saveString[PLAYER_NAME] as Int))!!
        this.energy = saveString[ENERGY_NAME] as Int
        sceneBrain = SceneBrain(player.brain)
        convoBrain = ConversationBrain(this)
        knownReports = (saveString[REPORTS_NAME] as List<Map<String, Any>>).map { map -> GlobalReportTypeFactory.fromMap(map) }.toMutableList()
        prospectiveActions = (saveString[REPORTS_NAME] as List<Map<String, Any>>).map { map -> Action(parent, map)}.toMutableList()
    }

    fun saveString(): Map<String, Any>{
        return hashMapOf(
            PLAYER_NAME to player.id,
            ENERGY_NAME to energy,
            REPORTS_NAME to knownReports.map{ report -> report.saveString()},
            ACTIONS_NAME to prospectiveActions.map { action -> action.saveString() }
        )
    }

    override fun equals(other: Any?): Boolean {
        if(other is ShortStateCharacter){
            return this.player.id == other.player.id
        } else {
            return false
        }
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