package shortstate.scenemaker

import shortstate.ShortGameScene
import shortstate.ShortStateGame

abstract class SceneMaker {

    abstract fun makeScene(game: ShortStateGame): ShortGameScene?

}