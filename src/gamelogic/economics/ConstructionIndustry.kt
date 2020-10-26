package gamelogic.economics

import game.Game
import gamelogic.territory.Territory
import kotlin.math.min

object ConstructionIndustry: Industry {
    override fun run(territory: Territory, game: Game, labor: Int): Int {

        territory.constructions.forEach { construction ->
            performConversions(territory, construction)

            construction.budget.resources.keys.forEach{resource ->
                val laborUsed = throughput(construction, resource)

                construction.budget.add(resource, -laborUsed)
                construction.spentResources.add(resource, laborUsed)
            }

            if(construction.complete()){
                territory.structures.add(construction.structure)
            }
        }

        territory.constructions.removeIf { it.complete() }


        return labor //for the time being, no labor gets used
    }

    private fun performConversions(territory: Territory, construction: Construction){
        val resourceConversions = territory.resourceConversions()
        val constructionCost = construction.structure.type.cost
        val resourcesExpended = construction.spentResources

        //first, let's trade anything that isn't used for building
        construction.budget.resources.filter{!constructionCost.resources.contains(it.key)}.forEach { spendingResource ->
            val optionsForThisResource = resourceConversions[spendingResource.key]

            if(optionsForThisResource != null){
                val conversionOptions = optionsForThisResource.entries
                    .filter { constructionCost[it.key] - resourcesExpended[it.key] > 0 }
                    .sortedByDescending { it.value / constructionCost[it.key] }

                if(conversionOptions.isNotEmpty()){
                    val bestOption = conversionOptions[0]
                    val amountToConvert = min((constructionCost[bestOption.key] - resourcesExpended[bestOption.key]) / bestOption.value, construction.budget[spendingResource.key])

                    construction.budget.add(bestOption.key, amountToConvert * bestOption.value)
                    construction.budget.add(spendingResource.key, -amountToConvert)
                }
            }
        }
    }

    private fun throughput(construction: Construction, resourceName: String): Int{
        return min(min(construction.budget.get(resourceName),construction.resourceNeeded(resourceName)), construction.structure.type.constructionThroughput)
    }
}