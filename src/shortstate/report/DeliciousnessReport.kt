package shortstate.report

import game.Game

class DeliciousnessReport: Report{

    override val type: String = GlobalReportTypeFactory.DELICIOUSNESS_REPORT_TYPE_NAME
    val value: Double
    
    constructor(deliciousness: Double){
        value = deliciousness
    }

    constructor(saveString: Map<String, Any>){
        value = saveString["value"] as Double
    }

    override fun apply(game: Game) {
       game.deliciousness = value
    }

    override fun type(): ReportType {
        return ReportType.DeliciousnessReportType
    }

    override fun toString(): String {
        return "Game has $value Deliciousness"
    }

    override fun specialSaveString(): Map<String, Any> {
        return hashMapOf(
            "value" to value
        )
    }
}