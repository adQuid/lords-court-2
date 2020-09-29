package gamelogic.territory

import game.Game
import main.UIGlobals
import shortstate.ShortStateCharacter
import shortstate.ShortStateGame
import shortstate.report.Report
import shortstate.report.ReportFactory

class ActiveCropsReport: Report {

    companion object{
        val type = "ActiveCropsReport"
    }

    override val type: String = ActiveCropsReport.type
    val territory: Territory
    val crops: List<Crop>
    
    constructor(game: Game, territory: Territory): super(game){
        this.territory = territory
        crops = territory.crops.map{Crop(it)}
    }

    constructor(saveString: Map<String, Any>, game: Game): super(saveString){
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
        if(crops.isEmpty()){
            return "As of ${context.game.turnName()}, ${territory.name} has not planted any crops"
        } else {
            return "As of ${context.game.turnName()}, ${territory.name} has ${crops.sumBy { it.quantity }} crops planted. "
        }
    }

    override fun detailedDescription(): String {

        if(crops.isEmpty()){
            return "There was either too little labor, too little seeds, or it's too late in the season."
        }

        val mostMatureCrop = crops.sortedByDescending { it.plantingTime }.first()
        val timeToHarvest = mostMatureCrop.plantingTime + mostMatureCrop.harvestAge() - UIGlobals.activeGame().turn

        val mostMatureCropText = if(timeToHarvest > 0) {"The most mature will be ready to harvest in ${mostMatureCrop} turns. "}else{"Crops are currently ready for harvest, once the peasants have time to reap them. "}

        return mostMatureCropText +
                "When all of these crops are harvested, we can expect to reap ${crops.sumBy { it.quantity * it.yield() }} seeds. They will still need to be ground into flower, " +
                "and some will spoil in this time. In addition, some seeds will be preserved to plant new crops in the future."
    }

    override fun specialSaveString(): Map<String, Any> {
        return hashMapOf(
            "territory" to territory.id,
            "crops" to crops.map { it.saveString() }
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
    override val cost: Int
        get() = 450

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