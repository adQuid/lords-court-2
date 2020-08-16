package aibrain.scenereactionadvocates

import game.Writ
import shortstate.ShortGameScene
import shortstate.ShortStateGame
import shortstate.room.Room
import shortstate.room.RoomAction
import shortstate.room.action.EnactWrit
import shortstate.room.action.Wait

class EnactWritAdvocate: SceneReactionAdvocate {

    private val me: game.GameCharacter

    constructor(character: game.GameCharacter) : super(character) {
        me = character
    }

    override fun weight(game: ShortStateGame, scene: ShortGameScene): Double {
        if(scene.room.type == Room.RoomType.THRONEROOM && writToEnact() != null){
            return me.brain.dealValueToMe(writToEnact()!!.deal) * 200.0
        }
        return 0.0
    }

    override fun doToScene(game: ShortStateGame, shortGameScene: ShortGameScene): RoomAction {
        if(writToEnact() != null){
            return EnactWrit(writToEnact()!!)
        } else {
            return Wait()
        }
    }

    private fun writToEnact(): Writ?{
        return me.writs.filter { writ -> writ.complete() && me.brain.dealValueToMe(writ.deal) > 0 }.getOrNull(0)
    }
}