package gamelogicmodules.capital.actionTypes

import game.action.Action
import game.Effect
import game.Game
import game.GameCharacter
import game.action.GlobalActionTypeFactory

class SetTaxRate: Action {

    companion object{
        val typeName: String
            get() = "SetTaxRate"
    }
    val terId: Int
    val amount: Double

    constructor(id: Int, amount: Double){
        this.terId = id
        this.amount = amount
    }

    override fun doAction(game: Game, player: GameCharacter): List<Effect> {
        return listOf()
    }

    override fun toString(): String {
        return "set taxes to ${amount}"
    }

    override fun equals(other: Any?): Boolean {
        return other is SetTaxRate
    }

    override fun saveString(): Map<String, Any> {
        return hashMapOf(
            GlobalActionTypeFactory.TYPE_NAME to typeName,
            "amount" to amount,
            "territory" to terId
        )
    }
}