package shortstate.dialog.linetypes

import aibrain.ConversationBrain
import shortstate.Conversation
import shortstate.dialog.Line
import shortstate.dialog.LineBlock
import shortstate.dialog.linetypes.traits.HasReportType
import shortstate.report.ReportType
import game.GameCharacter
import game.Game
import main.Controller
import shortstate.ShortStateCharacter
import shortstate.dialog.GlobalLineTypeFactory
import shortstate.dialog.linetypes.traits.HasAction
import ui.selectionmodal.SelectionModal
import ui.selectionmodal.Tab

class RequestReport: Line, HasReportType {

    override val type: String
        get() = GlobalLineTypeFactory.REQUEST_REPORT_TYPE_NAME
    var report: ReportType? = null

    constructor(report: ReportType?){
        this.report = report
    }

    constructor(map: Map<String, Any>){
        report = ReportType.valueOf(map["report"] as String)
    }

    override fun tooltipName(): String {
        return "Request Report"
    }

    override fun symbolicForm(speaker: ShortStateCharacter, target: ShortStateCharacter): List<LineBlock> {
        return listOf(LineBlock("REQUEST:"), LineBlock(if(report == null) "Report:___________" else "Report: "+report.toString(),
            {Controller.singleton!!.GUI!!.focusOn(
                SelectionModal(Controller.singleton!!.GUI!!,
                    listOf(Tab("Reports",Controller.singleton!!.GUI!!.playingAs().player.titles.flatMap { title -> title.reportsEntitled })),
                    { reportType ->
                        mySetReportType(reportType); Controller.singleton!!.GUI!!.defocus();
                    })
            )}))
    }

    override fun fullTextForm(speaker: ShortStateCharacter, target: ShortStateCharacter): String {
        return "What can you tell me of "+report?.toString()+"?"
    }

    override fun specialSaveString(): Map<String, Any> {
        return hashMapOf(
            GlobalLineTypeFactory.TYPE_NAME to "RequestReport",
            "report" to report.toString()
        )
    }

    override fun validToSend(): Boolean {
        return report != null
    }

    override fun possibleReplies(): List<Line> {
        return listOf(GiveReport(null)) //AI needs to think about matching reports manually
    }

    override fun mySetReportType(type: ReportType) {
        report = type
    }

    override fun myGetReportType(): ReportType? {
        return report
    }

    override fun specialEffect(conversation: Conversation) {
        //No special effects
    }

    override fun AIResponseFunction(brain: ConversationBrain, speaker: GameCharacter, game: Game): Line {
        if(brain.shortCharacter.knownReports.filter { report -> report.type() == myGetReportType() }.isNotEmpty()){
            return GiveReport(brain.shortCharacter.knownReports.filter { report -> report.type() == myGetReportType() }[0])
        } else {
            return Approve()
        }
    }
}