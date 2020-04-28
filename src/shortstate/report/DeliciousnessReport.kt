package shortstate.report

import game.Game
import game.gamelogicmodules.CookieWorld

class DeliciousnessReport: Report{

    override val type: String = GlobalReportTypeFactory.DELICIOUSNESS_REPORT_TYPE_NAME
    val value: Double
    
    constructor(game: Game){
        val logic = CookieWorld.getCookieWorld(game)
        value = logic.deliciousness
    }

    constructor(saveString: Map<String, Any>){
        value = saveString["value"] as Double
    }

    override fun apply(game: Game) {
        val logic = CookieWorld.getCookieWorld(game)
        logic.deliciousness = value
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DeliciousnessReport

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }
}