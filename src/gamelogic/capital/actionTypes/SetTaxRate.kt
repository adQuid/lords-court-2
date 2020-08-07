package gamelogic.capital.actionTypes

import game.action.Action
import game.Game
import game.GameCharacter
import game.action.GlobalActionTypeFactory
import gamelogic.capital.Capital
import gamelogic.capital.CapitalLogicModule
import gamelogic.territory.Territory
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import main.UIGlobals
import shortstate.ShortStateCharacter
import ui.componentfactory.UtilityComponentFactory
import java.text.DecimalFormat
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

class SetTaxRate: Action {

    companion object{
        val typeName: String
            get() = "SetTaxRate"
    }
    val terId: Int
    var amount: Double

    constructor(id: Int, amount: Double){
        this.terId = id
        this.amount = amount
    }

    override fun doAction(game: Game, player: GameCharacter){
        val logic = game.moduleOfType(CapitalLogicModule.type) as CapitalLogicModule

        logic.capitalById(terId).taxes[Territory.FLOUR_NAME] = amount
    }

    override fun tooltip(perspective: ShortStateCharacter): String {
        return "set taxes to ${DecimalFormat("#.##").format(amount)}"
    }

    override fun saveString(): Map<String, Any> {
        return hashMapOf(
            GlobalActionTypeFactory.TYPE_NAME to typeName,
            "amount" to amount,
            "territory" to terId
        )
    }

    override fun description(): String {
        return "Sets the tax for ${capitalById().territory!!.name} to ${(amount*100).roundToInt()}% of the harvest. This will be extracted from the peasants every harvest."
    }

    private fun capitalById(): Capital {
        val logic = UIGlobals.activeGame().moduleOfType(CapitalLogicModule.type) as CapitalLogicModule
        return logic.capitalById(terId)
    }

    override fun actionPane(action: Action, parent: MutableSet<Action>?): GridPane {
        val retval = baseActionPane(this, null)

        val amountPane = GridPane()
        amountPane.add(UtilityComponentFactory.proportionalButton("Less", EventHandler { amount = max(amount-0.05,0.0); UIGlobals.refresh() }, 3.0),0,0)
        amountPane.add(UtilityComponentFactory.shortProportionalLabel(DecimalFormat("#.##").format(amount), 3.0),1,0)
        amountPane.add(UtilityComponentFactory.proportionalButton("More", EventHandler { amount = min(amount+0.05,1.0); UIGlobals.refresh() }, 3.0),2,0)

        retval.add(amountPane,0,2)

        return retval
    }

    override fun universalDisplay(perspective: ShortStateCharacter?): Scene {


        return Scene(actionPane(this, null))
    }

    override fun equals(other: Any?): Boolean {
        return other is SetTaxRate && other.terId == terId && other.amount == amount
    }

    override fun collidesWith(other: Action): Boolean{
        return other is SetTaxRate && other.terId == this.terId
    }
}