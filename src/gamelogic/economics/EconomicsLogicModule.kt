package gamelogic.economics

import aibrain.Plan
import aibrain.Score
import game.Game
import game.GameCharacter
import game.GameLogicModule
import game.Location
import gamelogic.resources.ResourceTypes
import gamelogic.territory.Crop
import gamelogic.territory.Territory
import gamelogic.territory.TerritoryLogicModule
import gamelogic.territory.mapobjects.Structure
import kotlin.math.min

class EconomicsLogicModule: GameLogicModule {
    companion object {
        val type = "Economics"

        val LABOR_NAME = "labor"

        val industries = listOf(CropIndustry)
    }

    override val type = EconomicsLogicModule.type

    constructor(): super(listOf(), EconomicsTitleFactory(), listOf(TerritoryLogicModule.type)){

    }

    constructor(other: EconomicsLogicModule): super(listOf(), EconomicsTitleFactory(), listOf(TerritoryLogicModule.type)){

    }

    constructor(saveString: Map<String, Any>, game: Game): super(listOf(), EconomicsTitleFactory(), listOf(TerritoryLogicModule.type)){

    }

    override fun finishConstruction(game: Game) {
        //do nothing
    }

    override fun endTurn(game: Game) {

        val territoryLogic = game.moduleOfType(TerritoryLogicModule.type) as TerritoryLogicModule

        territoryLogic.territories().forEach {
            it.fleets.forEach { fleet ->
                fleet.ships.forEach{ship ->
                    ship.type.resourceExtractions.forEach {
                        game.characterById(fleet.owner).privateResources.add(it.key, it.value)
                    }
                }
            }
            it.structures.forEach { structure ->
                structure.manufatureTypeSelected = null
                structure.usesExpended = 0
            }
        }

        val laborByTerritory = territoryLogic.territories().associate { ter -> ter to ter.resources[ResourceTypes.POPULATION_NAME]!! }.toMutableMap()
        laborByTerritory.forEach {territory ->
            industries.forEach {
                industry -> laborByTerritory[territory.key] = industry.run(territory.key, game, territory.value)
            }
        }

    }

    fun bestConversion(territory: Territory, inputTypes: Collection<String>, outputType: String): Structure?{
        val retval = territory.structures.sortedByDescending { it.bestConversion(inputTypes, outputType)[outputType] }.firstOrNull()

        if(retval != null && retval!!.bestConversion(inputTypes, outputType).getOrDefault(outputType, 0) > 0){
            return retval
        } else {
            return null
        }
    }

    override fun locations(): Collection<Location> {
        return listOf()
    }

    override fun specialSaveString(): Map<String, Any> {
        return mapOf()
    }

    override fun planOptions(
        perspective: GameCharacter,
        importantPlayers: Collection<GameCharacter>
    ): Collection<Plan> {
        return listOf()
    }

    override fun score(perspective: GameCharacter): Score {
        return Score()
    }
}