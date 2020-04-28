package game

import game.gamelogicmodules.CookieWorld
import shortstate.report.GlobalReportTypeFactory
import shortstate.report.Report
import shortstate.report.ReportFactory
import java.lang.Exception

abstract class GameLogicModule {

    companion object{

        val TYPE_NAME = "TYPE"

        fun moduleFromSaveString(saveString: Map<String, Any>, game: Game): GameLogicModule{
            if(saveString[TYPE_NAME] == CookieWorld.type){
                return CookieWorld(saveString, game)
            }
            throw Exception("Logic module name: ${saveString.getOrDefault(TYPE_NAME, null)} not found!")
        }

        fun cloneModule(logicModule: GameLogicModule): GameLogicModule{
            if(logicModule is CookieWorld){
                return CookieWorld(logicModule as CookieWorld)
            }
            throw Exception("Can't figure out type: ${logicModule.javaClass}!")
        }
    }

    abstract val type: String

    var reportTypes: Map<String, ReportFactory>

    constructor(reportTypes: Map<String, ReportFactory>){
        this.reportTypes = reportTypes
    }

    abstract fun endTurn(): List<Effect>

    fun reportFromSaveString(saveString: Map<String, Any>, game: Game): Report?{
        if(reportTypes.containsKey(saveString[GlobalReportTypeFactory.TYPE_NAME])){
            return reportTypes[saveString[GlobalReportTypeFactory.TYPE_NAME]]!!.reportFromSaveString(saveString)
        }
        return null
    }

    fun reportFromType(type: String, game: Game): Report?{
        if(reportTypes.containsKey(type)){
            return reportTypes[type]!!.generateReport(game)
        }
        return null
    }

    fun saveString(): Map<String, Any> {
        return specialSaveString().plus(mapOf(GameLogicModule.TYPE_NAME to type))
    }

    abstract fun specialSaveString(): Map<String, Any>

    abstract fun value(perspective: GameCharacter): Double
}