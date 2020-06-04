package aibrain.scenereactionadvocates

import shortstate.ShortGameScene
import shortstate.ShortStateController
import shortstate.ShortStateGame
import shortstate.room.RoomAction

abstract class SceneReactionAdvocate {

    constructor(character: game.GameCharacter)

    abstract fun weight(game: ShortStateGame, shortGameScene: ShortGameScene): Double

    abstract fun doToScene(game: ShortStateGame, shortGameScene: ShortGameScene): RoomAction

}