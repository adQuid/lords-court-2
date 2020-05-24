package game

import gamelogicmodules.CookieWorld
import shortstate.report.GlobalReportTypeFactory
import shortstate.report.Report
import shortstate.report.ReportFactory
import java.lang.Exception
import gamelogicmodules.territory.TerritoryLogicModule
import gamelogicmodules.capital.CapitalLogicModule
import game.titlemaker.TitleFactory

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
    var titleTypes: TitleFactory
    var dependencies: List<String>

    constructor(reportTypes: Collection<ReportFactory>, titleTypes: TitleFactory, dependencies: List<String>){
        this.reportTypes = reportTypes.associate { it.type to it }
        this.titleTypes = titleTypes
        this.dependencies = dependencies
    }

    abstract fun finishConstruction(game: Game)

    abstract fun endTurn(game: Game): List<Effect>

    fun reportFromSaveString(saveString: Map<String, Any>, game: Game): Report?{
        if(reportTypes.containsKey(saveString[GlobalReportTypeFactory.TYPE_NAME])){
            return reportTypes[saveString[GlobalReportTypeFactory.TYPE_NAME]]!!.reportFromSaveString(saveString, game)
        }
        return null
    }

    fun reportFactoryFromType(type: String, game: Game): ReportFactory?{
        return reportTypes[type]
    }

    fun titleFromSaveString(saveString: Map<String, Any>, game: Game): Title?{
        return titleTypes.titleFromSaveString(saveString, game)
    }

    fun saveString(): Map<String, Any> {
        return specialSaveString().plus(mapOf(GameLogicModule.TYPE_NAME to type))
    }

    abstract fun specialSaveString(): Map<String, Any>

    abstract fun value(perspective: GameCharacter): Double
}