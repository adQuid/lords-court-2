package aibrain

import game.Effect
import game.Game
import game.GameCharacter

class DealCase {

    val deal: Deal

    constructor(deal: Deal){
        this.deal = deal.toFinishedDeal()
    }

    //returns the marginal multiple of previous value. For example, if the total of gamecase value was 100 before the deal and 120 after,
    //this would return 0.2
    fun dealValue(cases: List<GameCase>, perspectives: Collection<GameCharacter>): Map<GameCharacter, Double>{
        val baseValues = perspectives.associateWith { entry -> totalCaseValue(cases!!, entry)}
        val casesWithDeal = cases!!.map { it.applyDeal(deal) }

        return perspectives.associateWith { entry -> (totalCaseValue(casesWithDeal, entry) - baseValues[entry]!!) / baseValues[entry]!! }
    }

    fun dealScore(cases: List<GameCase>, perspectives: Collection<GameCharacter>): Map<GameCharacter, Score>{
        val casesWithDeal = cases!!.map { it.applyDeal(deal) }
        return perspectives.associateWith {entry -> totalCaseScore(casesWithDeal, entry).minus(totalCaseScore(cases!!, entry))}
    }

    private fun totalCaseValue(reality: List<GameCase>, player: GameCharacter): Double{
        return reality.fold(0.0, { acc, case -> acc + case.valueToCharacter(player) })
    }

    private fun totalCaseScore(reality: List<GameCase>, player: GameCharacter): Score{
        return reality.fold(Score(), { acc, case -> acc.plus(case.gameScore(player)) })
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