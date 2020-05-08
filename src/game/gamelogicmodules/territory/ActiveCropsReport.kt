package game.gamelogicmodules.territory

import game.Game
import shortstate.ShortStateCharacter
import shortstate.ShortStateGame
import shortstate.report.Report
import shortstate.report.ReportFactory

class ActiveCropsReport: Report {

    companion object{
        val type = "ActiveCropsReport"
    }

    override val type: String = FoodStocksReport.type
    val territory: Territory
    val crops: List<Crop>
    
    constructor(game: Game, territory: Territory){
        this.territory = territory
        crops = territory.crops.map{Crop(it)}
    }

    constructor(saveString: Map<String, Any>, game: Game){
        val logic = game.moduleOfType(TerritoryLogicModule.type) as TerritoryLogicModule

        territory = logic.territoryById(saveString["territory"] as Int)
        crops = (saveString["crops"] as List<Map<String, Any>>).map{map -> Crop(map)}.toMutableList()
    }

    override fun apply(game: Game) {
        val logic = game.moduleOfType(TerritoryLogicModule.type) as TerritoryLogicModule
        logic.matchingTerritory(territory).crops.clear()
        logic.matchingTerritory(territory).crops.addAll(crops)
    }

    override fun prettyPrint(context: ShortStateGame, perspective: ShortStateCharacter): String {
        return "As of ${context.game.turnName()}, ${territory.name} has ${crops.sumBy { it.quantity }} crops planted"
    }

    override fun specialSaveString(): Map<String, Any> {
        return hashMapOf(
            "territory" to territory.id,
            "crops" to crops
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FoodStocksReport

        if (territory != other.territory) return false

        return true
    }

    override fun hashCode(): Int {
        var result = territory.hashCode()
        result = 31 * result + crops.hashCode()
        return result
    }
}

class ActiveCropsReportFactory: ReportFactory{
    val territoryId: Int
    override val type = ActiveCropsReport.type

    constructor(territory: Int){
        this.territoryId = territory
    }

    override fun generateReport(game: Game): Report {
        val logic = game.moduleOfType(TerritoryLogicModule.type) as TerritoryLogicModule
        return ActiveCropsReport(game, logic.territoryById(territoryId))
    }

    override fun reportFromSaveString(saveString: Map<String, Any>, game: Game): Report {
        return ActiveCropsReport(saveString, game)
    }

    override fun tooltip(): String {
        return "Active Crops Report"
    }
}