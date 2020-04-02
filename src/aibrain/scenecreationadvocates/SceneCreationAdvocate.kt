package aibrain.scenecreationadvocates

import shortstate.ShortStateCharacter
import shortstate.ShortStateGame
import shortstate.scenemaker.SceneMaker

abstract class SceneCreationAdvocate {

    constructor(character: ShortStateCharacter)

    abstract fun weight(game: ShortStateGame): Double

    abstract fun createScene(game: ShortStateGame, player: ShortStateCharacter): SceneMaker

}