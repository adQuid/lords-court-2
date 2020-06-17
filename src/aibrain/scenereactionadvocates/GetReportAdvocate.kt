package aibrain.scenereactionadvocates

import shortstate.ShortGameScene
import shortstate.ShortStateController
import shortstate.ShortStateGame
import shortstate.report.DeliciousnessReport
import shortstate.report.DeliciousnessReportFactory
import shortstate.report.ReportFactory
import shortstate.report.ReportType
import shortstate.room.Room
import shortstate.room.RoomAction
import shortstate.room.action.MakeReport

class GetReportAdvocate: SceneReactionAdvocate {

    private val me: game.GameCharacter

    constructor(character: game.GameCharacter) : super(character) {
        me = character
    }

    override fun weight(game: ShortStateGame, scene: ShortGameScene): Double {
        if(reportFactories().isEmpty() || scene.room.type != Room.RoomType.WORKROOM){
            return 0.0
        }
        return 15.0 - (15.0 * scene.shortPlayerForLongPlayer(me)!!.knownReports.size)
    }

    override fun doToScene(game: ShortStateGame, shortGameScene: ShortGameScene): RoomAction {
        //TODO: Make this better
        return MakeReport(reportFactories().first())
    }

    private fun reportFactories(): List<ReportFactory>{
        return me.reportsEntitled().toList()
    }
}