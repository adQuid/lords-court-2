package aibrain.scenereactionadvocates

import shortstate.ShortGameScene
import shortstate.ShortStateController
import shortstate.ShortStateGame
import shortstate.room.RoomAction
import shortstate.room.action.EndScene

class LeaveSceneAdvocate: SceneReactionAdvocate {

    val me: game.GameCharacter

    constructor(character: game.GameCharacter) : super(character) {
        me = character
    }

    override fun weight(game: ShortStateGame, shortGameScene: ShortGameScene): Double {
        return game.shortPlayerForLongPlayer(me)!!.sceneBrain.bestCreationAdvocate(game).weight(game).weight - 0.01
    }

    override fun doToScene(game: ShortStateGame, shortGameScene: ShortGameScene): RoomAction {
        return EndScene()
    }
}