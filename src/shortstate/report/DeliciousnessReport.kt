package shortstate.report

import game.Game
import gamelogic.cookieworld.CookieWorld
import shortstate.ShortStateCharacter
import shortstate.ShortStateGame

class DeliciousnessReport: Report{

    companion object{
        val type = "Deliciousness"
    }

    override val type: String = DeliciousnessReport.type
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

    override fun prettyPrint(context: ShortStateGame, perspective: ShortStateCharacter): String {
        return "Game has $value Deliciousness"
    }

    override fun detailedDescription(): String {
        return "TEST"
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

class DeliciousnessReportFactory: ReportFactory() {
    override val type = DeliciousnessReport.type
    override val cost: Int
        get() = 15

    override fun generateReport(game: Game): Report {
        return DeliciousnessReport(game)
    }

    override fun reportFromSaveString(saveString: Map<String, Any>, game: Game): Report {
        return DeliciousnessReport(saveString)
    }

    override fun tooltip(): String {
        return "Deliciousness Report"
    }
}