package shortstate.report

import game.Game

enum class ReportType{
    DeliciousnessReportType
}

fun generateReport(game: Game, type: ReportType): Report{
    if(type == ReportType.DeliciousnessReportType){
        return DeliciousnessReport(game.deliciousness)
    }

    throw Exception("Report Type not recognized!")
}