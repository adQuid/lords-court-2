package aibrain.scenereactionadvocates

import aibrain.Deal
import shortstate.ShortGameScene
import shortstate.ShortStateController
import shortstate.ShortStateGame
import shortstate.report.ReportType
import shortstate.room.Room
import shortstate.room.action.DraftWrit
import shortstate.room.action.MakeReport

class DraftWritAdvocate: SceneReactionAdvocate {

    private val me: game.GameCharacter

    constructor(character: game.GameCharacter) : super(character) {
        me = character
    }

    override fun weight(game: ShortStateGame, scene: ShortGameScene): Double {
        if(scene.room.type != Room.RoomType.WORKROOM){
            return 0.0
        }

        if(dealIWantToDraft() != null){
            return me.brain.dealsILike!![dealIWantToDraft()]!! * 100.0
        }
        return 0.0
    }

    override fun doToScene(game: ShortStateGame, shortGameScene: ShortGameScene) {
        println("drafting a writ")
        if(dealIWantToDraft() != null){
            DraftWrit(dealIWantToDraft()!!, "test name").doActionIfCanAfford(game, game.shortPlayerForLongPlayer(me)!!)
        }
    }

    private fun dealIWantToDraft(): Deal?{
        val dealsToOffer = me.brain.dealsILike!!.keys.filter { deal -> me.writs.filter { writ -> writ.deal == deal }.isEmpty() }
        if(dealsToOffer.isNotEmpty()){
            return dealsToOffer[0]
        }
        return null
    }
}