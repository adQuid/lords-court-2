package shortstate

import aibrain.ConversationBrain
import aibrain.SceneBrain
import game.GameCharacter
import game.Game
import javafx.scene.Scene
import shortstate.linetriggers.LineTrigger
import shortstate.report.Report
import shortstate.room.RoomAction
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
    val DONE_NAME = "done"
    var done = false

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
        done = saveString[DONE_NAME] as Boolean
        sceneBrain = SceneBrain(this, player.brain)
        convoBrain = ConversationBrain(this)

    }

    fun saveString(): Map<String, Any>{
        return hashMapOf(
            PLAYER_NAME to player.id,
            ENERGY_NAME to energy,
            DONE_NAME to done
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

    override fun display(perspective: ShortStateCharacter): Scene {
        //remember, the constructor is the one being looked at
        return CharacterDetailComponentFactory(this).characterFocusPage(perspective)
    }

    fun canAffordAction(action: RoomAction): Boolean{
        return action.cost() == 0 || action.cost() <= energy
    }
}