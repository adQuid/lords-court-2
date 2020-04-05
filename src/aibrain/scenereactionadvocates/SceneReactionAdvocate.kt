package aibrain.scenereactionadvocates

import shortstate.ShortGameScene
import shortstate.ShortStateController

abstract class SceneReactionAdvocate {

    constructor(character: game.GameCharacter)

    abstract fun weight(shortStateController: ShortStateController, shortGameScene: ShortGameScene): Double

    abstract fun doToScene(shortStateController: ShortStateController, shortGameScene: ShortGameScene)

}