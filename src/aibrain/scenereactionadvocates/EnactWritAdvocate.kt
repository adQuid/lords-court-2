package aibrain.scenereactionadvocates

import game.Writ
import shortstate.ShortGameScene
import shortstate.ShortStateGame
import shortstate.room.Room
import shortstate.room.RoomAction
import shortstate.room.action.EnactWrit

class EnactWritAdvocate: SceneReactionAdvocate {

    private val me: game.GameCharacter

    constructor(character: game.GameCharacter) : super(character) {
        me = character
    }

    override fun weight(game: ShortStateGame, scene: ShortGameScene): Double {
        if(scene.room.type == Room.RoomType.THRONEROOM && writToEnact() != null){
            return me.brain.dealsILike!!.getOrDefault(writToEnact()!!.deal,0.0) * 200.0
        }
        return 0.0
    }

    override fun doToScene(game: ShortStateGame, shortGameScene: ShortGameScene): RoomAction {
        return EnactWrit(writToEnact()!!)
    }

    private fun writToEnact(): Writ?{
        return me.writs.filter { writ -> writ.complete() && me.brain.dealsILike!!.containsKey(writ.deal) }.getOrNull(0)
    }
}