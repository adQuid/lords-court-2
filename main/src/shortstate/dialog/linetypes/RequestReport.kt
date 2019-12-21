package shortstate.dialog.linetypes

import shortstate.Conversation
import shortstate.dialog.Line
import shortstate.dialog.LineBlock
import shortstate.dialog.linetypes.traits.HasReportType
import shortstate.report.ReportType

class RequestReport: Line, HasReportType {

    var report: ReportType? = null

    constructor(report: ReportType?){
        this.report = report
    }

    override fun tooltipName(): String {
        return "Request Report"
    }

    override fun symbolicForm(): List<LineBlock> {
        return listOf(LineBlock("REQUEST:"), LineBlock(if(report == null) "Report:___________" else "Report: "+report.toString()))
    }

    override fun fullTextForm(): String {
        return "What can you tell me of "+report?.toString()+"?"
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
}