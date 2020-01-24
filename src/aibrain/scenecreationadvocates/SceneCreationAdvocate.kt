package aibrain.scenecreationadvocates

import shortstate.ShortStateGame
import shortstate.scenemaker.SceneMaker

abstract class SceneCreationAdvocate {

    constructor(character: game.GameCharacter)

    abstract fun weight(game: ShortStateGame): Double

    abstract fun createScene(game: ShortStateGame, player: shortstate.ShortStateCharacter): SceneMaker

}