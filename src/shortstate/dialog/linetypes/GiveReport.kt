package shortstate.dialog.linetypes

import aibrain.ConversationBrain
import shortstate.Conversation
import shortstate.dialog.Line
import shortstate.dialog.LineBlock
import shortstate.report.Report
import game.GameCharacter
import game.Game
import shortstate.ShortStateCharacter
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

    override fun symbolicForm(speaker: ShortStateCharacter, target: ShortStateCharacter): List<LineBlock> {
        return listOf(LineBlock("GIVE REPORT:"), LineBlock(if(report == null) "Report:___________" else "Report: "+report.toString()))
    }

    override fun fullTextForm(speaker: ShortStateCharacter, target: ShortStateCharacter): String {
        return "I have discovered that ${report.toString()}"
    }

    override fun specialSaveString(): Map<String, Any> {
        return hashMapOf(
            "report" to report!!.saveString()
        )
    }

    override fun validToSend(): Boolean {
        return report != null
    }

    override fun possibleReplies(): List<Line> {
        return listOf(Approve(), Disapprove())
    }

    override fun specialEffect(conversation: Conversation, speaker: ShortStateCharacter) {
        conversation.lastSpeaker.knownReports.add(report!!)
    }

    override fun AIResponseFunction(brain: ConversationBrain, speaker: GameCharacter, game: Game): Line {
        throw NotImplementedError()
    }
}