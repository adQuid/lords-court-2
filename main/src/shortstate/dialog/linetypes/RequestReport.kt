package shortstate.dialog.linetypes

import game.action.Action
import shortstate.dialog.Line
import shortstate.dialog.LineBlock
import shortstate.report.Report

class RequestReport: Line {

    var report: Report? = null

    constructor(report: Report?){
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
        return listOf()
    }
}