package gamelogic.government.actionTypes

import game.Game
import game.GameCharacter
import game.action.Action
import gamelogic.government.GovernmentLogicModule
import gamelogic.government.Law
import gamelogic.government.laws.GlobalLawFactory
import shortstate.ShortStateCharacter

class EnactLaw: Action {

    companion object{
        val typeName = "enactLaw"
    }

    val lawToEnact: Law
    val capitalId: Int

    constructor(law: Law, capital: Int){
        this.lawToEnact = law
        this.capitalId = capital
    }

    constructor(saveString: Map<String,Any>){
        this.capitalId = saveString["capital"] as Int
        this.lawToEnact = GlobalLawFactory.lawFromSaveString(saveString["law"] as Map<String, Any>)
    }

    override fun isLegal(game: Game, player: GameCharacter): Boolean {
        return player.titles.flatMap { it.actionsReguarding(listOf(player)) }.flatMap { it.items }.filter { it is EnactLaw && it.capitalId == capitalId }.isNotEmpty()
    }

    override fun doAction(game: Game, player: GameCharacter) {
        val logic = game.moduleOfType(GovernmentLogicModule.type) as GovernmentLogicModule
        logic.capitalById(capitalId).enactLaw(lawToEnact)
    }

    override fun saveString(): Map<String, Any> {
        return mapOf(
            "capital" to capitalId,
            "law" to lawToEnact.saveString()
        )
    }

    override fun collidesWith(other: Action): Boolean {
        if(other is EnactLaw){
            return this.lawToEnact.type == other.lawToEnact.type
        }
        return false
    }

    override fun tooltip(perspective: ShortStateCharacter): String {
        return "Enact ${lawToEnact.type}"
    }

    override fun description(): String {
        return "Enact ${lawToEnact.type}"
    }

}