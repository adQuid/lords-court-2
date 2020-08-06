package aibrain.scenereactionadvocates

import shortstate.ShortGameScene
import shortstate.ShortStateController
import shortstate.ShortStateGame
import shortstate.room.Room
import shortstate.room.RoomAction
import shortstate.room.action.GoToBed
import shortstate.room.action.Wait

class WaitAdvocate: SceneReactionAdvocate {

    private val me: game.GameCharacter

    constructor(character: game.GameCharacter) : super(character) {
        me = character
    }

    override fun weight(game: ShortStateGame, shortGameScene: ShortGameScene): Double {
        if(shortGameScene.conversation == null){
            return 0.5
        } else {
            return 0.0
        }
    }

    override fun doToScene(game: ShortStateGame, shortGameScene: ShortGameScene):RoomAction {
        return Wait()
    }
}