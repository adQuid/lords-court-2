package shortstate.dialog.linetypes

import shortstate.report.ReportType

interface HasReportType {
    fun mySetReportType(type: ReportType)

    fun myGetReportType(): ReportType?
}