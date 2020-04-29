package shortstate.report

object GlobalReportTypeFactory {

    val EMPTY_REPORT_TYPE_NAME = "None"
    val DELICIOUSNESS_REPORT_TYPE_NAME = DeliciousnessReport.type

    val TYPE_NAME = "TYPE"
    val typeMap: HashMap<String, (map: Map<String, Any>) -> Report> = hashMapOf(
        DELICIOUSNESS_REPORT_TYPE_NAME to { map -> DeliciousnessReport(map) }
    )

    fun fromMap(map: Map<String, Any>): Report {
        return typeMap[map[TYPE_NAME]]!!(map)
    }
}