package gamelogicmodules.capital.actionTypes

import game.action.Action
import game.Effect
import game.Game
import game.GameCharacter
import game.action.GlobalActionTypeFactory
import game.effects.GlobalEffectFactory
import gamelogicmodules.capital.CapitalLogicModule
import gamelogicmodules.cookieworld.CookieWorld
import gamelogicmodules.territory.Territory

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
        return listOf(ChangeTax(1.0, terId, amount))
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

    class ChangeTax(override var probability: Double, val terId: Int, val amount: Double) : Effect() {

        companion object{
            val typeName = "cngeTax"
        }

        constructor(saveString: Map<String, Any>, game: Game) : this(saveString[GlobalEffectFactory.PROBABLITY_NAME] as Double, saveString["terId"] as Int, saveString["amount"] as Double) {

        }

        override fun equals(other: Any?): Boolean {
            if(other is ChangeTax){
                return true
            }
            return false
        }

        override fun apply(game: Game) {
            val logic = game.moduleOfType(CapitalLogicModule.type) as CapitalLogicModule

            logic.capitalById(terId).taxes[Territory.FLOUR_NAME] = amount
        }
        override fun toString(): String{
            return "set taxes to ${amount}"
        }

        override fun saveString(): Map<String, Any> {
            return hashMapOf(
                GlobalEffectFactory.TYPE_NAME to typeName,
                GlobalEffectFactory.PROBABLITY_NAME to probability
            )
        }

        override fun describe(): String {
            return "taxes would be set to $amount"
        }
    }
}