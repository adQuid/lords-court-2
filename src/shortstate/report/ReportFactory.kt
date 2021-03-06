package shortstate.report

import game.Game

enum class ReportType{
    NoneReportType, DeliciousnessReportType
}

fun generateEmptyReport(): Report{
    return EmptyReport()
}

abstract class ReportFactory {
    abstract val type: String
    abstract val cost: Int

    abstract fun generateReport(game: Game): Report

    abstract fun reportFromSaveString(saveString: Map<String, Any>, game: Game): Report

    override fun equals(other: Any?): Boolean {
        if(other == null) return false
        return this::class == other::class
    }

    override fun hashCode(): Int {
        return 1
    }

    override fun toString(): String {
        return tooltip()
    }

    abstract fun tooltip(): String
}