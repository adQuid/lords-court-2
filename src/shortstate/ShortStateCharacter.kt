package shortstate

import aibrain.ConversationBrain
import aibrain.SceneBrain
import game.GameCharacter
import game.Game
import javafx.scene.Scene
import shortstate.report.Report
import shortstate.scenemaker.SceneMaker
import ui.PerspectiveDisplayable
import ui.componentfactory.CharacterDetailComponentFactory

class ShortStateCharacter: PerspectiveDisplayable {

    val PLAYER_NAME = "PLAYER"
    val player: GameCharacter
    var sceneBrain: SceneBrain
    var convoBrain: ConversationBrain
    val ENERGY_NAME = "ENERGY"
    var energy: Int
    val REPORTS_NAME = "REPORTS"
    var knownReports = mutableListOf<Report>()

    var nextSceneIWannaBeIn: SceneMaker? = null

    constructor(player: GameCharacter){
        this.player = player
        this.energy = 1000
        sceneBrain = SceneBrain(this, player.brain)
        convoBrain = ConversationBrain(this)
    }

    constructor(parent: Game, saveString: Map<String, Any>){
        this.player = parent.matchingPlayer(parent.characterById(saveString[PLAYER_NAME] as Int))!!
        this.energy = saveString[ENERGY_NAME] as Int
        sceneBrain = SceneBrain(this, player.brain)
        convoBrain = ConversationBrain(this)
        knownReports = (saveString[REPORTS_NAME] as List<Map<String, Any>>).map { map -> parent.reportFromMap(map) }.toMutableList()
    }

    fun saveString(): Map<String, Any>{
        return hashMapOf(
            PLAYER_NAME to player.id,
            ENERGY_NAME to energy,
            REPORTS_NAME to knownReports.map{ report -> report.saveString()}
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
        if(player.brain.lastCasesOfConcern == null){
            player.brain.thinkAboutNextTurn(game.game)
        }
        nextSceneIWannaBeIn = sceneBrain.nextSceneIWantToBeIn(this, game)
    }

    fun reportOfType(type: String): Report? {
        return knownReports.filter { report -> report.type == type }.getOrNull(0)
    }

    override fun display(perspective: ShortStateCharacter): Scene {
        //remember, the constructor is the one being looked at
        return CharacterDetailComponentFactory(this).characterFocusPage(perspective)
    }
}