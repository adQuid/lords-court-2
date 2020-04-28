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

    abstract fun generateReport(game: Game): Report

    abstract fun reportFromSaveString(saveString: Map<String, Any>): Report

    override fun equals(other: Any?): Boolean {
        if(other == null) return false
        return this::class == other::class
    }

    override fun hashCode(): Int {
        return 1
    }
}