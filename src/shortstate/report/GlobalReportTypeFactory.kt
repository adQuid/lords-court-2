package shortstate.report

object GlobalReportTypeFactory {

    val DELICIOUSNESS_REPORT_TYPE_NAME = "Delic"

    val TYPE_NAME = "TYPE"
    val typeMap: HashMap<String, (map: Map<String, Any>) -> Report> = hashMapOf(
        DELICIOUSNESS_REPORT_TYPE_NAME to { map -> DeliciousnessReport(map) }
    )

    fun fromMap(map: Map<String, Any>): Report {
        return typeMap[map[TYPE_NAME]]!!(map)
    }
}