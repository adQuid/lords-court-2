package aibrain.scenereactionadvocates

import shortstate.Scene
import shortstate.ShortStateGame
import shortstate.report.ReportType
import shortstate.room.action.MakeReport

class GetReportAdvocate: SceneReactionAdvocate {

    private val me: game.Character

    constructor(character: game.Character) : super(character) {
        me = character
    }

    override fun weight(scene: Scene): Double {
        return 15.0 - (15.0 * scene.shortPlayerForLongPlayer(me)!!.knownReports.size)
    }

    override fun doToScene(game: ShortStateGame, scene: Scene) {
        MakeReport(ReportType.DeliciousnessReportType).doAction(game, scene!!.shortPlayerForLongPlayer(me)!!)
    }
}