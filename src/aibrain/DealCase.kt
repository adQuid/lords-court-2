package aibrain

import game.GameCharacter

class DealCase {

    val deal: Deal

    constructor(deal: Deal){
        this.deal = deal
    }

    //returns the marginal multiple of previous value. For example, if the total of gamecase value was 100 before the deal and 120 after,
    //this would return 0.2
    fun dealValue(cases: List<GameCase>): Map<GameCharacter, Double>{
        val casesWithDeal = cases!!.map { it.applyDeal(deal) }
        return deal.theActions().entries.associate { entry ->
            entry.key to (totalCaseValue(casesWithDeal, entry.key) - totalCaseValue(cases!!, entry.key)) / totalCaseValue(cases!!, entry.key) }
    }

    private fun totalCaseValue(reality: List<GameCase>, player: GameCharacter): Double{
        return reality.fold(0.0, { acc, case -> acc + case.valueToCharacter(player) })
    }

}