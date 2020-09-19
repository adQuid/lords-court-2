package aibrain

import game.GameCharacter
import gamelogic.government.GovernmentLogicModule
import gamelogic.resources.Resources
import gamelogic.territory.Territory
import gamelogic.territory.TerritoryLogicModule

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

        val debug1 = cases[0].currentGame.moduleOfType(GovernmentLogicModule.type) as GovernmentLogicModule
        val debug2 = casesWithDeal[0].currentGame.moduleOfType(GovernmentLogicModule.type) as GovernmentLogicModule

        return perspectives.associateWith {entry -> totalCaseScore(casesWithDeal, entry).minus(totalCaseScore(cases!!, entry))}
    }

    private fun totalCaseValue(reality: List<GameCase>, player: GameCharacter): Double{
        return reality.fold(0.0, { acc, case -> acc + case.valueToCharacter(player) })
    }

    private fun totalCaseScore(reality: List<GameCase>, player: GameCharacter): Score{
        return reality.fold(Score(), { acc, case -> acc.plus(case.gameScore(player)) })
    }

    fun marginalScore(cases: List<GameCase>, players: Collection<GameCharacter>): Score{
        val casesWithDeal = cases.map{ it.applyDeal(deal)}

        val scoreWithoutDeal = Score(players.flatMap {player -> cases.flatMap { case -> case.gameScore(player).components() }})
        val scoreWithDeal = Score(players.flatMap {player -> casesWithDeal.flatMap { case -> case.gameScore(player).components() }})

        return scoreWithDeal.minus(scoreWithoutDeal)
    }

}