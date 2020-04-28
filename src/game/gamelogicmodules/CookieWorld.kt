package game.gamelogicmodules

import game.Effect
import game.Game
import game.GameCharacter
import game.GameLogicModule
import shortstate.report.DeliciousnessReport
import shortstate.report.DeliciousnessReportFactory
import shortstate.report.GlobalReportTypeFactory
import shortstate.report.ReportType

class CookieWorld: GameLogicModule {

    companion object{
        val type = "Cookieworld"

        fun getCookieWorld(game: Game): CookieWorld{
            return game.moduleOfType("Cookieworld") as CookieWorld
        }
    }

    override val type = CookieWorld.type

    val DELICIOUSNESS_NAME = "deliciousness"
    var deliciousness = 0.0
    val HAS_MILK_NAME = "hasMilk"
    var hasMilk = mutableListOf<GameCharacter>()

    constructor() : super(mapOf(
        GlobalReportTypeFactory.DELICIOUSNESS_REPORT_TYPE_NAME to DeliciousnessReportFactory()
    )) {
        deliciousness = 0.0
        hasMilk = mutableListOf<GameCharacter>()
    }

    constructor(other: CookieWorld): super(mapOf(
        GlobalReportTypeFactory.DELICIOUSNESS_REPORT_TYPE_NAME to DeliciousnessReportFactory()
    )){
        deliciousness = other.deliciousness
        hasMilk = other.hasMilk.toMutableList()
    }

    constructor(saveString: Map<String, Any>, game: Game): super(mapOf(
        GlobalReportTypeFactory.DELICIOUSNESS_REPORT_TYPE_NAME to DeliciousnessReportFactory()
    )){
        deliciousness = saveString[DELICIOUSNESS_NAME] as Double
        hasMilk = (saveString[HAS_MILK_NAME] as List<Int>).map { id -> game.characterById(id)}.toMutableList()
    }

    override fun endTurn(): List<Effect> {
        return listOf()
    }

    override fun value(perspective: GameCharacter): Double {
        var retval = 0.0
        if(hasMilk.contains(perspective)){
            retval += deliciousness * 1.0
        } else {
            retval += 0.0
        }
        return retval
    }

    override fun specialSaveString(): Map<String, Any> {
        return mapOf(
            DELICIOUSNESS_NAME to deliciousness,
            HAS_MILK_NAME to hasMilk.map { it.id }
        )
    }
}