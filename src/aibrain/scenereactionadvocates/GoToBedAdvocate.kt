package aibrain.scenereactionadvocates

import shortstate.ShortGameScene
import shortstate.ShortStateController
import shortstate.ShortStateGame
import shortstate.room.Room
import shortstate.room.RoomAction
import shortstate.room.action.GoToBed

class GoToBedAdvocate: SceneReactionAdvocate {

    private val me: game.GameCharacter

    constructor(character: game.GameCharacter) : super(character) {
        me = character
    }

    override fun weight(game: ShortStateGame, shortGameScene: ShortGameScene): Double {
        return if(game.shortPlayerForLongPlayer(me)!!.energy < 300){ return 2.0} else {return 0.0}
    }

    override fun doToScene(game: ShortStateGame, shortGameScene: ShortGameScene):RoomAction {
        return GoToBed()
    }
}