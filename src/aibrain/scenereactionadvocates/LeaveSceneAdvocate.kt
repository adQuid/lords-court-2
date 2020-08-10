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
        if(shortGameScene.conversation == null){
            return game.shortPlayerForLongPlayer(me)!!.sceneBrain.bestCreationAdvocate(game).weight(game).weight - 0.01
        } else {
            return 0.0
        }
    }

    override fun doToScene(game: ShortStateGame, shortGameScene: ShortGameScene): RoomAction {
        return EndScene()
    }
}