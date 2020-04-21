package aibrain.scenereactionadvocates

import shortstate.ShortGameScene
import shortstate.ShortStateController
import shortstate.ShortStateGame

class LeaveSceneAdvocate: SceneReactionAdvocate {

    val me: game.GameCharacter

    constructor(character: game.GameCharacter) : super(character) {
        me = character
    }

    override fun weight(game: ShortStateGame, shortGameScene: ShortGameScene): Double {
        return 0.5
    }

    override fun doToScene(game: ShortStateGame, shortGameScene: ShortGameScene) {
        shortGameScene.terminated = true
    }
}