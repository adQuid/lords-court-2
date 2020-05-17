package aibrain

import game.Effect
import game.Game
import game.GameCharacter

class DealCase {

    val deal: Deal

    constructor(deal: Deal){
        this.deal = deal
    }

    //returns the marginal multiple of previous value. For example, if the total of gamecase value was 100 before the deal and 120 after,
    //this would return 0.2
    fun dealValue(cases: List<GameCase>, perspectives: Collection<GameCharacter>): Map<GameCharacter, Double>{
        val casesWithDeal = cases!!.map { it.applyDeal(deal) }
        val effectsLost = cases.mapIndexed{index, case -> case.finalEffects.filterIndexed{eIndex, effect -> casesWithDeal[index].finalEffects.getOrNull(eIndex) != effect}}
        val effectsGained = casesWithDeal.mapIndexed{index, case -> case.finalEffects.filterIndexed{eIndex, effect -> cases[index].finalEffects.getOrNull(eIndex) != effect}}
        return perspectives.associateWith { entry -> (totalCaseValue(casesWithDeal, entry) - totalCaseValue(cases!!, entry)) / totalCaseValue(cases!!, entry) }
    }

    private fun totalCaseValue(reality: List<GameCase>, player: GameCharacter): Double{
        return reality.fold(0.0, { acc, case -> acc + case.valueToCharacter(player) })
    }

    fun effectsOfDeal(cases: List<GameCase>): List<Effect>{
        val casesWithDeal = cases.map{ it.applyDeal(deal)}
        casesWithDeal.forEach { case -> case.finalEffects.forEach { effect -> effect.probability *= case.probability() } }
        val allEffects =  casesWithDeal.flatMap { it.finalEffects }
        val compressedEffects = mutableListOf<Effect>()
        allEffects.forEach {
            effect -> if(compressedEffects.contains(effect)){
                compressedEffects.first { it == effect }.probability += effect.probability
            } else {
                compressedEffects.add(effect)
            }
        }
        return compressedEffects
    }

}