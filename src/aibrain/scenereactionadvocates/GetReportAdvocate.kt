package aibrain.scenereactionadvocates

import shortstate.ShortGameScene
import shortstate.ShortStateController
import shortstate.report.ReportType
import shortstate.room.Room
import shortstate.room.action.MakeReport

class GetReportAdvocate: SceneReactionAdvocate {

    private val me: game.GameCharacter

    constructor(character: game.GameCharacter) : super(character) {
        me = character
    }

    override fun weight(shortStateController: ShortStateController, scene: ShortGameScene): Double {
        if(scene.room.type != Room.RoomType.WORKROOM){
            return 0.0
        }
        return 15.0 - (15.0 * scene.shortPlayerForLongPlayer(me)!!.knownReports.size)
    }

    override fun doToScene(shortStateController: ShortStateController, shortGameScene: ShortGameScene) {
        MakeReport(ReportType.DeliciousnessReportType).doAction(shortStateController, shortGameScene!!.shortPlayerForLongPlayer(me)!!)
    }
}