package aibrain.scenereactionadvocates

import shortstate.ShortGameScene
import shortstate.ShortStateController
import shortstate.room.Room
import shortstate.room.action.GoToBed

class GoToBedAdvocate: SceneReactionAdvocate {

    private val me: game.GameCharacter

    constructor(character: game.GameCharacter) : super(character) {
        me = character
    }

    override fun weight(shortStateController: ShortStateController, shortGameScene: ShortGameScene): Double {
        if(shortGameScene.room.type == Room.RoomType.BEDROOM && shortGameScene.characters.size == 1){
            return 900.0 - shortGameScene.characters[0].energy //TODO: should I really be inferring my own identity like this?
        }
        return 0.0
    }

    override fun doToScene(shortStateController: ShortStateController, shortGameScene: ShortGameScene) {
        GoToBed().doAction(shortStateController, shortGameScene!!.shortPlayerForLongPlayer(me)!!)
    }
}