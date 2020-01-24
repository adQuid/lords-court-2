package aibrain.scenereactionadvocates

import shortstate.ShortGameScene
import shortstate.ShortStateController

class LeaveSceneAdvocate: SceneReactionAdvocate {

    val me: game.GameCharacter

    constructor(character: game.GameCharacter) : super(character) {
        me = character
    }

    override fun weight(shortGameScene: ShortGameScene): Double {
        return 1.0
    }

    override fun doToScene(shortStateController: ShortStateController, shortGameScene: ShortGameScene) {
        shortStateController.endScene(shortGameScene)
    }
}