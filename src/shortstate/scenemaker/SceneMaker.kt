package shortstate.scenemaker

import shortstate.Scene
import shortstate.ShortStateGame

abstract class SceneMaker {

    abstract fun makeScene(game: ShortStateGame): Scene?

}