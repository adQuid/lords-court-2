package aibrain.scenereactionadvocates

import shortstate.Scene
import shortstate.ShortStateCharacter
import shortstate.ShortStateController
import shortstate.ShortStateGame

class LeaveSceneAdvocate: SceneReactionAdvocate {

    val me: game.Character

    constructor(character: game.Character) : super(character) {
        me = character
    }

    override fun weight(scene: Scene): Double {
        return 1.0
    }

    override fun doToScene(shortStateController: ShortStateController, scene: Scene) {
        shortStateController.endScene(scene)
    }
}