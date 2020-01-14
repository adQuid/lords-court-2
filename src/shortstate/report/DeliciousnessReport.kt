package shortstate.report

import game.Game

class DeliciousnessReport: Report{
    
    val value: Double
    
    constructor(deliciousness: Double){
        value = deliciousness
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

    override fun saveString(): Map<String, Any> {
        return hashMapOf(
            "value" to value
        )
    }
}