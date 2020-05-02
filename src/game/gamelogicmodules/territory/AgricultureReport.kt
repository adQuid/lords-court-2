package game.gamelogicmodules.territory

import game.Game
import shortstate.ShortStateCharacter
import shortstate.ShortStateGame
import shortstate.report.Report
import shortstate.report.ReportFactory

class AgricultureReport: Report {

    companion object{
        val type = "AgriculturalReport"
    }

    override val type: String = AgricultureReport.type
    val territory: Territory
    val flour: Int
    val bread: Int
    
    constructor(game: Game, territory: Territory){
        this.territory = territory
        flour = territory.resources[territory.FLOUR_NAME]!!
        bread = territory.resources[territory.BREAD_NAME]!!
    }

    constructor(saveString: Map<String, Any>, game: Game){
        val logic = game.moduleOfType(TerritoryLogicModule.type) as TerritoryLogicModule

        territory = logic.territoryById(saveString["territory"] as Int)
        flour = saveString["flour"] as Int
        bread = saveString["bread"] as Int
    }

    override fun apply(game: Game) {
        val logic = game.moduleOfType(TerritoryLogicModule.type) as TerritoryLogicModule
        logic.matchingTerritory(territory).resources[territory.FLOUR_NAME] = flour
        logic.matchingTerritory(territory).resources[territory.BREAD_NAME] = bread
    }

    override fun prettyPrint(context: ShortStateGame, perspective: ShortStateCharacter): String {
        return "${(context.game.moduleOfType(TerritoryLogicModule.type) as TerritoryLogicModule).weekName(context.game.turn)}: ${territory.name} has $flour flour and $bread bread"
    }

    override fun specialSaveString(): Map<String, Any> {
        return hashMapOf(
            "territory" to territory.id,
            "flour" to flour,
            "bread" to bread
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AgricultureReport

        if (territory != other.territory) return false
        if (flour != other.flour) return false
        if (bread != other.bread) return false

        return true
    }

    override fun hashCode(): Int {
        var result = territory.hashCode()
        result = 31 * result + flour
        result = 31 * result + bread
        return result
    }
}

class AgricultureReportFactory: ReportFactory{
    val territory: Territory
    override val type = AgricultureReport.type

    constructor(territory: Territory){
        this.territory = territory
    }

    override fun generateReport(game: Game): Report {
        val logic = game.moduleOfType(TerritoryLogicModule.type) as TerritoryLogicModule
        return AgricultureReport(game, territory)
    }

    override fun reportFromSaveString(saveString: Map<String, Any>, game: Game): Report {
        return AgricultureReport(saveString, game)
    }

}