package game

import aibrain.Plan
import aibrain.Score
import gamelogic.cookieworld.CookieWorld
import shortstate.report.GlobalReportTypeFactory
import shortstate.report.Report
import shortstate.report.ReportFactory
import java.lang.Exception
import gamelogic.territory.TerritoryLogicModule
import gamelogic.government.GovernmentLogicModule
import game.titlemaker.TitleFactory
import gamelogic.economics.EconomicsLogicModule
import gamelogic.playerresources.PlayerResourceModule
import javafx.scene.control.Button
import shortstate.ShortStateCharacter

abstract class GameLogicModule {

    companion object{

        val TYPE_NAME = "TYPE"

        fun moduleFromSaveString(saveString: Map<String, Any>, game: Game): GameLogicModule{
            if(saveString[TYPE_NAME] == PlayerResourceModule.type){
                return PlayerResourceModule(saveString, game)
            }
            if(saveString[TYPE_NAME] == CookieWorld.type){
                return CookieWorld(saveString, game)
            }
            if(saveString[TYPE_NAME] == TerritoryLogicModule.type){
                return TerritoryLogicModule(saveString, game)
            }
            if(saveString[TYPE_NAME] == EconomicsLogicModule.type){
                return EconomicsLogicModule(saveString, game)
            }
            if(saveString[TYPE_NAME] == GovernmentLogicModule.type){
                return GovernmentLogicModule(saveString, game)
            }
            throw Exception("Logic module name: ${saveString.getOrDefault(TYPE_NAME, null)} not found!")
        }

        fun cloneModule(logicModule: GameLogicModule, game: Game): GameLogicModule{
            if(logicModule is CookieWorld){
                return CookieWorld(logicModule)
            }
            if(logicModule is PlayerResourceModule){
                return PlayerResourceModule(logicModule)
            }
            if(logicModule is TerritoryLogicModule){
                return TerritoryLogicModule(logicModule)
            }
            if(logicModule is EconomicsLogicModule){
                return EconomicsLogicModule(logicModule)
            }
            if(logicModule is GovernmentLogicModule){
                return GovernmentLogicModule(logicModule, game)
            }
            throw Exception("Can't figure out type: ${logicModule.javaClass}!")
        }
    }

    abstract val type: String

    var parent: Game? = null
    var reportTypes: Map<String, ReportFactory>
    var titleTypes: TitleFactory
    var dependencies: List<String>

    constructor(reportTypes: Collection<ReportFactory>, titleTypes: TitleFactory, dependencies: List<String>){
        this.reportTypes = reportTypes.associate { it.type to it }
        this.titleTypes = titleTypes
        this.dependencies = dependencies
    }

    abstract fun finishConstruction(game: Game)

    abstract fun endTurn(game: Game)

    abstract fun locations(): Collection<Location>

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

    abstract fun planOptions(perspective: GameCharacter, importantPlayers: Collection<GameCharacter>): Collection<Plan>

    abstract fun score(perspective: GameCharacter): Score
}