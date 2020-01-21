package shortstate.dialog.linetypes

import aibrain.ConversationBrain
import shortstate.Conversation
import shortstate.dialog.Line
import shortstate.dialog.LineBlock
import shortstate.report.Report
import game.Character
import game.Game
import shortstate.dialog.GlobalLineTypeFactory
import shortstate.report.GlobalReportTypeFactory

class GiveReport: Line {

    override val type: String
        get() = GlobalLineTypeFactory.GIVE_REPORT_TYPE_NAME
    var report: Report?

    constructor(report: Report?){
        this.report = report
    }

    constructor(saveString: Map<String, Any>){
        report = GlobalReportTypeFactory.fromMap(saveString["report"] as Map<String, Any>)
    }

    override fun tooltipName(): String {
        return "Submit Report"
    }

    override fun symbolicForm(): List<LineBlock> {
        return listOf(LineBlock("GIVE REPORT:"), LineBlock(if(report == null) "Report:___________" else "Report: "+report.toString()))
    }

    override fun fullTextForm(speaker: Character, target: Character): String {
        return "I have discovered that ${report.toString()}"
    }

    override fun specialSaveString(): Map<String, Any> {
        return hashMapOf(
            GlobalLineTypeFactory.TYPE_NAME to "GiveReport",
            "report" to report!!.saveString()
        )
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

    override fun AIResponseFunction(brain: ConversationBrain, speaker: Character, game: Game): Line {
        throw NotImplementedError()
    }
}