package shortstate.report

import game.Game

class EmptyReport: Report() {

    override val type: String = GlobalReportTypeFactory.EMPTY_REPORT_TYPE_NAME

    override fun apply(game: Game) {
        //Do nothing
    }

    override fun specialSaveString(): Map<String, Any> {
        return mapOf()
    }

    override fun toString(): String {
        return "I haven't had a chance to check that"
    }

    override fun equals(other: Any?): Boolean {
        return javaClass != other?.javaClass
    }
}