package shortstate.report

import game.Game
import shortstate.ShortStateCharacter
import shortstate.ShortStateGame

class EmptyReport: Report() {

    override val type: String = GlobalReportTypeFactory.EMPTY_REPORT_TYPE_NAME

    override fun apply(game: Game) {
        //Do nothing
    }

    override fun specialSaveString(): Map<String, Any> {
        return mapOf()
    }

    override fun prettyPrint(context: ShortStateGame, perspective: ShortStateCharacter): String {
        return "I haven't had a chance to check that"
    }

    override fun detailedDescription(): String {
        return "This is a placeholder report. How did you get this?"
    }

    override fun equals(other: Any?): Boolean {
        return javaClass != other?.javaClass
    }
}