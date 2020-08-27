package shortstate.scenemaker

import shortstate.GameRules
import shortstate.ShortGameScene
import shortstate.ShortStateGame

abstract class SceneMaker {

    abstract fun makeScene(game: ShortStateGame): ShortGameScene?

    open fun cost(): Int{
        return GameRules.COST_TO_MAKE_SCENE
    }

}