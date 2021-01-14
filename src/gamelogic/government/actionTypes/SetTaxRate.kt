package gamelogic.government.actionTypes

import game.action.Action
import game.Game
import game.GameCharacter
import game.action.GlobalActionTypeFactory
import gamelogic.government.Capital
import gamelogic.government.GovernmentLogicModule
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
import gamelogic.government.Count
import gamelogic.resources.ResourceTypes

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

    override fun isLegal(game: Game, player: GameCharacter): Boolean {
        return player.titles.flatMap { it.actionsReguarding(listOf(player)) }.flatMap { it.items }.filter { it is SetTaxRate && it.terId == terId }.isNotEmpty()
    }

    override fun doAction(game: Game, player: GameCharacter){
        val logic = game.moduleOfType(GovernmentLogicModule.type) as GovernmentLogicModule

        logic.capitalById(terId).taxes[ResourceTypes.SEEDS_NAME] = amount
    }

    override fun tooltip(perspective: ShortStateCharacter): String {
        return "set tax rate"
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
        val logic = UIGlobals.activeGame().moduleOfType(GovernmentLogicModule.type) as GovernmentLogicModule
        return logic.capitalById(terId)
    }

    override fun actionPane(action: Action): GridPane {
        val retval = baseActionPane(this)

        retval.add(UtilityComponentFactory.proportionalLabel(action.description(), 1.0, 0.7),0,1)

        val amountPane = GridPane()
        amountPane.add(UtilityComponentFactory.proportionalButton("Less", EventHandler { amount = max(amount-0.05,0.0); UIGlobals.refresh() }, 1.0/3.0),0,0)
        amountPane.add(UtilityComponentFactory.shortProportionalLabel(DecimalFormat("#.##").format(amount), 3.0),1,0)
        amountPane.add(UtilityComponentFactory.proportionalButton("More", EventHandler { amount = min(amount+0.05,1.0); UIGlobals.refresh() }, 1.0/3.0),2,0)

        retval.add(amountPane,0,8)

        return retval
    }

    override fun universalDisplay(perspective: ShortStateCharacter?): Scene {


        return Scene(actionPane(this))
    }

    override fun equals(other: Any?): Boolean {
        return other is SetTaxRate && other.terId == terId && other.amount == amount
    }

    override fun collidesWith(other: Action): Boolean{
        return other is SetTaxRate && other.terId == this.terId
    }
}