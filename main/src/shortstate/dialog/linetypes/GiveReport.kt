package shortstate.dialog.linetypes

import shortstate.Conversation
import shortstate.dialog.Line
import shortstate.dialog.LineBlock
import shortstate.report.Report

class GiveReport: Line {

    var report: Report?

    constructor(report: Report?){
        this.report = report
    }

    override fun tooltipName(): String {
        return "Submit Report"
    }

    override fun symbolicForm(): List<LineBlock> {
        return listOf(LineBlock("GIVE REPORT:"), LineBlock(if(report == null) "Report:___________" else "Report: "+report.toString()))
    }

    override fun fullTextForm(): String {
        return "I have discovered that ${report.toString()}"
    }

    override fun validToSend(): Boolean {
        return report != null
    }

    override fun possibleReplies(): List<Line> {
        return listOf(Approve(), Disapprove())
    }

    override fun specialEffect(conversation: Conversation) {
        conversation.lastSpeaker.knownReports.add(report!!)
    }
}