package gamelogic.economics

import game.Game
import gamelogic.territory.Territory
import kotlin.math.min

object ConstructionIndustry: Industry {
    override fun run(territory: Territory, game: Game, labor: Int): Int {

        territory.constructions.forEach { construction ->
            //TODO: Add resource exchanges

            val laborUsed = min(min(construction.budget.get(EconomicsLogicModule.LABOR_NAME),construction.resourceNeeded(EconomicsLogicModule.LABOR_NAME)), construction.structure.type.constructionThroughput)

            construction.budget.add(EconomicsLogicModule.LABOR_NAME, -laborUsed)
            construction.spentResources.add(EconomicsLogicModule.LABOR_NAME, laborUsed)

            if(construction.complete()){
                territory.structures.add(construction.structure)
            }
        }

        territory.constructions.removeIf { it.complete() }


        return labor //for the time being, no labor gets used
    }
}