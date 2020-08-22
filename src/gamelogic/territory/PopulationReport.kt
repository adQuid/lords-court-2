package gamelogic.territory

import game.Game
import shortstate.ShortStateCharacter
import shortstate.ShortStateGame
import shortstate.report.Report
import shortstate.report.ReportFactory

class PopulationReport: Report {

    companion object{
        val type = "PopulationReport"
    }

    override val type: String = PopulationReport.type
    val territory: Territory
    val population: Int
    
    constructor(game: Game, territory: Territory){
        this.territory = territory
        population = territory.resources.get(Territory.POPULATION_NAME)
    }

    constructor(saveString: Map<String, Any>, game: Game){
        val logic = game.moduleOfType(TerritoryLogicModule.type) as TerritoryLogicModule

        territory = logic.territoryById(saveString["territory"] as Int)
        population = saveString["pop"] as Int
    }

    override fun apply(game: Game) {
        val logic = game.moduleOfType(TerritoryLogicModule.type) as TerritoryLogicModule
        logic.matchingTerritory(territory).resources.set(Territory.POPULATION_NAME, population)
    }

    override fun prettyPrint(context: ShortStateGame, perspective: ShortStateCharacter): String {
        return "As of ${context.game.turnName()}, ${territory.name} has ${population} population"
    }

    override fun detailedDescription(): String {
        return "Each population will require one food of every type to stay healthy."
    }

    override fun specialSaveString(): Map<String, Any> {
        return hashMapOf(
            "territory" to territory.id,
            "pop" to population
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PopulationReport

        if (territory != other.territory) return false

        return true
    }

    override fun hashCode(): Int {
        var result = territory.hashCode()
        result = 31 * result + population.hashCode()
        return result
    }
}

class PopulationReportFactory: ReportFactory{
    val territoryId: Int
    override val type = PopulationReport.type
    override val cost: Int
        get() = 150

    constructor(territory: Int){
        this.territoryId = territory
    }

    override fun generateReport(game: Game): Report {
        val logic = game.moduleOfType(TerritoryLogicModule.type) as TerritoryLogicModule
        return PopulationReport(game, logic.territoryById(territoryId))
    }

    override fun reportFromSaveString(saveString: Map<String, Any>, game: Game): Report {
        return PopulationReport(saveString, game)
    }

    override fun tooltip(): String {
        return "Population Report"
    }
}