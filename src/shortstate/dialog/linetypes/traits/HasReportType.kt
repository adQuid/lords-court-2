package shortstate.dialog.linetypes.traits

import shortstate.report.ReportType

interface HasReportType {
    fun mySetReportType(type: ReportType)

    fun myGetReportType(): ReportType?
}