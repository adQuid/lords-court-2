package shortstate.dialog.linetypes

import aibrain.ConversationBrain
import shortstate.Conversation
import shortstate.dialog.Line
import shortstate.dialog.LineBlock
import shortstate.report.Report
import game.GameCharacter
import game.Game
import main.UIGlobals
import shortstate.ShortStateCharacter
import shortstate.ShortStateGame
import shortstate.dialog.GlobalLineTypeFactory
import shortstate.report.EmptyReport
import shortstate.report.GlobalReportTypeFactory
import shortstate.report.ReportType
import shortstate.room.Room
import ui.specialdisplayables.selectionmodal.SelectionModal
import ui.specialdisplayables.selectionmodal.Tab

class GiveReport: Line {

    override val type: String
        get() = GlobalLineTypeFactory.GIVE_REPORT_TYPE_NAME
    var report: Report?

    constructor(report: Report?){
        this.report = report
    }

    constructor(saveString: Map<String, Any>, game: Game){
        report = game.reportFromMap(saveString["report"] as Map<String, Any>)
    }

    override fun tooltipName(): String {
        if(report != null && report!! is EmptyReport){
            return "No Report"
        }
        return "Submit Report"
    }

    override fun symbolicForm(context: ShortStateGame, speaker: ShortStateCharacter, target: ShortStateCharacter): List<LineBlock> {
        return listOf(LineBlock("GIVE REPORT:"), LineBlock(if(report == null) "Report:___________" else "Report: "+ report!!.type, { perspective -> UIGlobals.focusOn(
            SelectionModal("Select Report",
                listOf(
                Tab(
                    "Reports",
                    perspective.knownReports)
            ), { selectedReport -> this.report = selectedReport; UIGlobals.defocus() }
            )
        )}))
    }

    override fun fullTextForm(context: ShortStateGame, speaker: ShortStateCharacter, target: ShortStateCharacter): String {
        if(report is EmptyReport){
            return (report as EmptyReport).prettyPrint(context, speaker)
        } else if(report != null) {
            return "I have discovered that ${report!!.prettyPrint(context,speaker)}"
        } else {
            return "I have discovered that ________"
        }
    }

    override fun tooltipDescription(): String {
        return "Hand over report so the character can consider it in future decisions."
    }

    override fun specialSaveString(): Map<String, Any> {
        return hashMapOf(
            "report" to report!!.saveString()
        )
    }

    override fun validToSend(): Boolean {
        return report != null
    }

    override fun canChangeTopic(): Boolean {
        return false
    }

    override fun possibleReplies(perspective: ShortStateCharacter, other: ShortStateCharacter): List<Line> {
        return listOf(Approve(), Disapprove())
    }

    override fun specialEffect(room: Room, shortGame: ShortStateGame, speaker: ShortStateCharacter) {
        if(!(report is EmptyReport)){
            shortGame.shortGameScene!!.conversation!!.lastSpeaker.knownReports.add(report!!)
        }
    }

    override fun AIResponseFunction(brain: ConversationBrain, speaker: ShortStateCharacter, game: Game): Line {
        throw NotImplementedError()
    }
}