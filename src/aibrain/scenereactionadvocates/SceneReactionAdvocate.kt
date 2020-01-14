package aibrain.scenereactionadvocates

import shortstate.Scene
import shortstate.ShortStateGame

abstract class SceneReactionAdvocate {

    constructor(character: game.Character)

    abstract fun weight(scene: Scene): Double

    abstract fun doToScene(game: ShortStateGame, scene: Scene)

}