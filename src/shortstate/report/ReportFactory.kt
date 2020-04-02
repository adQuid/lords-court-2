package shortstate.report

import game.Game

enum class ReportType{
    NoneReportType, DeliciousnessReportType
}

fun generateReport(game: Game, type: ReportType): Report{
    if(type == ReportType.DeliciousnessReportType){
        return DeliciousnessReport(game.deliciousness)
    }

    throw Exception("Report Type not recognized!")
}

fun generateEmptyReport(): Report{
    return EmptyReport()
}