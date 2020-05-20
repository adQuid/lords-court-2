package shortstate.dialog.linetypes

import aibrain.ConversationBrain
import shortstate.Conversation
import shortstate.dialog.Line
import shortstate.dialog.LineBlock
import shortstate.dialog.linetypes.traits.HasReportType
import shortstate.report.ReportType
import game.GameCharacter
import game.Game
import main.UIGlobals
import shortstate.ShortStateCharacter
import shortstate.ShortStateGame
import shortstate.dialog.GlobalLineTypeFactory
import shortstate.report.EmptyReport
import shortstate.report.generateEmptyReport
import shortstate.room.Room
import ui.specialdisplayables.selectionmodal.SelectionModal
import ui.specialdisplayables.selectionmodal.Tab

class RequestReport: Line, HasReportType {

    override val type: String
        get() = GlobalLineTypeFactory.REQUEST_REPORT_TYPE_NAME
    var report: String? = null

    constructor(report: String?){
        this.report = report
    }

    constructor(map: Map<String, Any>){
        report = map["report"] as String
    }

    override fun tooltipName(): String {
        return "Request Report"
    }

    override fun symbolicForm(context: ShortStateGame, speaker: ShortStateCharacter, target: ShortStateCharacter): List<LineBlock> {
        return listOf(LineBlock("REQUEST:"), LineBlock(if(report == null) "SELECT REPORT" else "Report: "+report.toString(),
            {
                UIGlobals.focusOn(
                    SelectionModal( "Select Report",
                        listOf(
                            Tab(
                                "Reports",
                                UIGlobals.playingAs().player.titles.flatMap { title -> title.reportsEntitled })
                        ),
                        { reportType ->
                            mySetReportType(reportType.type); UIGlobals.defocus();
                        })
            )}))
    }

    override fun fullTextForm(context: ShortStateGame, speaker: ShortStateCharacter, target: ShortStateCharacter): String {
        return "What can you tell me of "+if(report != null) report.toString() else "______"+"?"
    }

    override fun specialSaveString(): Map<String, Any> {
        return hashMapOf(
            "report" to report.toString()
        )
    }

    override fun validToSend(): Boolean {
        return report != null
    }

    override fun canChangeTopic(): Boolean {
        return false
    }

    override fun possibleReplies(perspective: ShortStateCharacter): List<Line> {
        return listOf(GiveReport(EmptyReport()), GiveReport(perspective.reportOfType(report!!)))
    }

    override fun mySetReportType(type: String) {
        report = type
    }

    override fun myGetReportType(): String? {
        return report
    }

    override fun specialEffect(room: Room, conversation: Conversation, speaker: ShortStateCharacter) {
        //No special effects
    }

    override fun AIResponseFunction(brain: ConversationBrain, speaker: GameCharacter, game: Game): Line {
        val relatedReport = brain.shortCharacter.reportOfType(report!!)
        if(relatedReport != null){
            return GiveReport(relatedReport)
        } else {
            return GiveReport(generateEmptyReport())
        }
    }
}