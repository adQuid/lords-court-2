package aibrain.scenereactionadvocates

import shortstate.ShortGameScene
import shortstate.ShortStateController
import shortstate.report.ReportType
import shortstate.room.action.MakeReport

class GetReportAdvocate: SceneReactionAdvocate {

    private val me: game.GameCharacter

    constructor(character: game.GameCharacter) : super(character) {
        me = character
    }

    override fun weight(shortGameScene: ShortGameScene): Double {
        return 15.0 - (15.0 * shortGameScene.shortPlayerForLongPlayer(me)!!.knownReports.size)
    }

    override fun doToScene(shortStateController: ShortStateController, shortGameScene: ShortGameScene) {
        MakeReport(ReportType.DeliciousnessReportType).doAction(shortStateController, shortGameScene!!.shortPlayerForLongPlayer(me)!!)
    }
}