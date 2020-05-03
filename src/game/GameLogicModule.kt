package game

import game.gamelogicmodules.CookieWorld
import shortstate.report.GlobalReportTypeFactory
import shortstate.report.Report
import shortstate.report.ReportFactory
import java.lang.Exception
import game.gamelogicmodules.territory.TerritoryLogicModule
import game.gamelogicmodules.capital.CapitalLogicModule

abstract class GameLogicModule {

    companion object{

        val TYPE_NAME = "TYPE"

        fun moduleFromSaveString(saveString: Map<String, Any>, game: Game): GameLogicModule{
            if(saveString[TYPE_NAME] == CookieWorld.type){
                return CookieWorld(saveString, game)
            }
            if(saveString[TYPE_NAME] == TerritoryLogicModule.type){
                return TerritoryLogicModule(saveString, game)
            }
            if(saveString[TYPE_NAME] == CapitalLogicModule.type){
                return CapitalLogicModule(saveString, game)
            }
            throw Exception("Logic module name: ${saveString.getOrDefault(TYPE_NAME, null)} not found!")
        }

        fun cloneModule(logicModule: GameLogicModule, game: Game): GameLogicModule{
            if(logicModule is CookieWorld){
                return CookieWorld(logicModule)
            }
            if(logicModule is TerritoryLogicModule){
                return TerritoryLogicModule(logicModule)
            }
            if(logicModule is CapitalLogicModule){
                return CapitalLogicModule(logicModule, game)
            }
            throw Exception("Can't figure out type: ${logicModule.javaClass}!")
        }
    }

    abstract val type: String

    var reportTypes: Map<String, ReportFactory>

    constructor(reportTypes: Collection<ReportFactory>){
        this.reportTypes = reportTypes.associate { it.type to it }
    }

    abstract fun finishConstruction(game: Game)

    abstract fun endTurn(): List<Effect>

    fun reportFromSaveString(saveString: Map<String, Any>, game: Game): Report?{
        if(reportTypes.containsKey(saveString[GlobalReportTypeFactory.TYPE_NAME])){
            return reportTypes[saveString[GlobalReportTypeFactory.TYPE_NAME]]!!.reportFromSaveString(saveString, game)
        }
        return null
    }

    fun reportFactoryFromType(type: String, game: Game): ReportFactory?{
        return reportTypes[type]
    }

    fun saveString(): Map<String, Any> {
        return specialSaveString().plus(mapOf(GameLogicModule.TYPE_NAME to type))
    }

    abstract fun specialSaveString(): Map<String, Any>

    abstract fun value(perspective: GameCharacter): Double
}