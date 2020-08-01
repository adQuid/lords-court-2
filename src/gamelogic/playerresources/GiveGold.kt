package gamelogic.playerresources

import game.action.Action
import game.Effect
import game.Game
import game.GameCharacter
import game.action.GlobalActionTypeFactory
import gamelogic.cookieworld.effects.CookieWorldEffectFactory
import gamelogic.capital.Capital
import gamelogic.capital.CapitalLogicModule
import gamelogic.territory.Territory
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import jdk.jshell.execution.Util
import main.UIGlobals
import shortstate.ShortStateCharacter
import ui.componentfactory.UtilityComponentFactory
import ui.specialdisplayables.selectionmodal.SelectionModal
import ui.specialdisplayables.selectionmodal.Tab
import java.text.DecimalFormat
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

class GiveGold: Action {

    companion object{
        val typeName: String
            get() = "GiveGold"
    }
    var characterId: Int
    var amount: Int

    constructor(id: Int, amount: Int){
        this.characterId = id
        this.amount = amount
    }

    fun characterName(): String{
        return UIGlobals.activeGame().characterById(characterId).name
    }

    override fun doAction(game: Game, player: GameCharacter): List<Effect> {
        return listOf(ChangeGold(1.0, characterId, amount))
    }

    override fun tooltip(perspective: ShortStateCharacter): String {
        return "give ${amount} Gold"
    }

    override fun saveString(): Map<String, Any> {
        return hashMapOf(
            GlobalActionTypeFactory.TYPE_NAME to typeName,
            "amount" to amount,
            "territory" to characterId
        )
    }

    override fun description(): String {
        return "Give ${amount} gold to ${characterName()}"
    }

    override fun universalDisplay(perspective: ShortStateCharacter?): Scene {
        val retval = baseActionPane(this, null)

        val targetPane = GridPane()
        targetPane.add(UtilityComponentFactory.shortWideButton("Give to: ${characterName()}", EventHandler { UIGlobals.focusOn(SelectionModal("Recipient", listOf(
            Tab("Recipient",UIGlobals.activeGame().players)), {character -> characterId = character.id; UIGlobals.defocus()}
        )) }), 0, 0)

        val amountPane = GridPane()
        amountPane.add(UtilityComponentFactory.proportionalButton("50 Less", EventHandler { amount = max(amount-50,0); UIGlobals.refresh() }, 6.0),0,0)
        amountPane.add(UtilityComponentFactory.proportionalButton("Less", EventHandler { amount = max(amount-1,0); UIGlobals.refresh() }, 6.0),1,0)
        amountPane.add(UtilityComponentFactory.shortProportionalLabel(DecimalFormat("#.##").format(amount), 3.0),2,0)
        amountPane.add(UtilityComponentFactory.proportionalButton("More", EventHandler { amount = min(amount+1,perspective!!.player.resources.get(PlayerResourceTypes.GOLD_NAME)); UIGlobals.refresh() }, 6.0),3,0)
        amountPane.add(UtilityComponentFactory.proportionalButton("50 More", EventHandler { amount = min(amount+50,perspective!!.player.resources.get(PlayerResourceTypes.GOLD_NAME)); UIGlobals.refresh() }, 6.0),4,0)

        retval.add(targetPane, 0,2)
        retval.add(amountPane,0,3)

        return Scene(retval)
    }

    override fun equals(other: Any?): Boolean {
        return other is GiveGold && other.characterId == characterId && other.amount == amount
    }

    override fun collidesWith(other: Action): Boolean{
        return other is GiveGold && other.characterId == this.characterId
    }

    class ChangeGold(override var probability: Double, val characterId: Int, val amount: Int) : Effect() {

        companion object{
            val typeName = "cngeTax"
        }

        constructor(saveString: Map<String, Any>, game: Game) : this(saveString[CookieWorldEffectFactory.PROBABLITY_NAME] as Double, saveString["terId"] as Int, saveString["amount"] as Int) {

        }

        override fun equals(other: Any?): Boolean {
            if(other is ChangeGold){
                return other.characterId == characterId && other.amount == amount
            }
            return false
        }

        override fun apply(game: Game) {
            val logic = game.moduleOfType(CapitalLogicModule.type) as CapitalLogicModule

            game.characterById(characterId).resources.add(PlayerResourceTypes.GOLD_NAME, amount)
        }
        override fun toString(): String{
            return "give ${amount} gold to ${characterId}"
        }

        override fun saveString(): Map<String, Any> {
            return hashMapOf(
                CookieWorldEffectFactory.TYPE_NAME to typeName,
                CookieWorldEffectFactory.PROBABLITY_NAME to probability
            )
        }

        override fun description(): String {
            return "${characterId} would get $amount gold"
        }
    }
}