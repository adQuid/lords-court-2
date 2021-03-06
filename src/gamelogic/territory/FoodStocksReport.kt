package gamelogic.territory

import game.Game
import gamelogic.resources.ResourceTypes
import shortstate.ShortStateCharacter
import shortstate.ShortStateGame
import shortstate.report.Report
import shortstate.report.ReportFactory

class FoodStocksReport: Report {

    companion object{
        val type = "FoodStocksReport"
    }

    override val type: String = FoodStocksReport.type
    val territory: Territory
    val flour: Int
    val bread: Int
    
    constructor(game: Game, territory: Territory): super(game){
        this.territory = territory
        println(territory.name)
        flour = territory.resources.get(ResourceTypes.FLOUR_NAME)
        bread = territory.resources.get(ResourceTypes.BREAD_NAME)
    }

    constructor(saveString: Map<String, Any>, game: Game): super(saveString){
        val logic = game.moduleOfType(TerritoryLogicModule.type) as TerritoryLogicModule

        territory = logic.territoryById(saveString["territory"] as Int)
        if(saveString["flour"] == null){
            println("debug")
        }
        flour = saveString["flour"] as Int
        bread = saveString["bread"] as Int
    }

    override fun apply(game: Game) {
        val logic = game.moduleOfType(TerritoryLogicModule.type) as TerritoryLogicModule
        logic.matchingTerritory(territory).resources.set(ResourceTypes.FLOUR_NAME, flour)
        logic.matchingTerritory(territory).resources.set(ResourceTypes.BREAD_NAME, bread)
    }

    override fun prettyPrint(context: ShortStateGame, perspective: ShortStateCharacter): String {
        return "As of ${context.game.turnName()}, ${territory.name} has $flour flour and $bread bread"
    }

    override fun detailedDescription(): String {
        return "These resources belong to the population, and will need to be taxed or seized before they can be used by the government. Each unit of flour can be baked into 2 units of bread, which the peasants will do as needed to feed themselves. One bread per turn is enough to keep one population healthy."
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

        other as FoodStocksReport

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

class FoodStocksReportFactory: ReportFactory{
    val territoryId: Int
    override val type = FoodStocksReport.type
    override val cost: Int
        get() = 100

    constructor(territory: Int){
        this.territoryId = territory
    }

    override fun generateReport(game: Game): Report {
        val logic = game.moduleOfType(TerritoryLogicModule.type) as TerritoryLogicModule
        return FoodStocksReport(game, logic.territoryById(territoryId))
    }

    override fun reportFromSaveString(saveString: Map<String, Any>, game: Game): Report {
        return FoodStocksReport(saveString, game)
    }

    override fun tooltip(): String {
        return "Food Stocks Report"
    }
}