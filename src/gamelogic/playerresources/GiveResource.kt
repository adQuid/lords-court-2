package gamelogic.playerresources

import game.action.Action
import game.Game
import game.GameCharacter
import game.action.GlobalActionTypeFactory
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import main.UIGlobals
import shortstate.ShortStateCharacter
import ui.componentfactory.UtilityComponentFactory
import ui.specialdisplayables.selectionmodal.SelectionModal
import ui.specialdisplayables.selectionmodal.Tab
import java.text.DecimalFormat
import kotlin.math.max
import kotlin.math.min

class GiveResource: Action {

    companion object{
        val typeName: String
            get() = "GiveGold"
    }
    var characterId: Int
    var resource: String
    var amount: Int

    constructor(id: Int, resource: String, amount: Int){
        this.characterId = id
        this.resource = resource
        this.amount = amount
    }

    fun characterName(): String{
        return UIGlobals.activeGame().characterById(characterId).name
    }

    override fun isLegal(game: Game, player: GameCharacter): Boolean {
        return player.resources.get(resource) >= amount
    }

    override fun doAction(game: Game, player: GameCharacter){
        if(player.resources.get(resource) >= amount){
            player.resources.add(resource, -amount)
            game.characterById(characterId).resources.add(resource, amount)
        }
    }

    override fun tooltip(perspective: ShortStateCharacter): String {
        return "give ${amount} ${resource}"
    }

    override fun saveString(): Map<String, Any> {
        return hashMapOf(
            GlobalActionTypeFactory.TYPE_NAME to typeName,
            "amount" to amount,
            "resource" to resource,
            "char" to characterId
        )
    }

    override fun description(): String {
        return "Give ${amount} ${resource} to ${characterName()}"
    }

    override fun actionPane(action: Action): GridPane{
        val retval = baseActionPane(this)

        retval.add(UtilityComponentFactory.proportionalLabel(action.description(), 1.0, 0.6),0,1)

        val targetPane = GridPane()
        targetPane.add(UtilityComponentFactory.shortWideButton("Give to: ${characterName()}", EventHandler { UIGlobals.focusOn(SelectionModal("Recipient", listOf(
            Tab("Recipient",UIGlobals.activeGame().players)), {character -> characterId = character.id; UIGlobals.defocus()}
        )) }), 0, 0)

        val amountPane = GridPane()
        amountPane.add(UtilityComponentFactory.proportionalButton("50 Less", EventHandler { amount = max(amount-50,0); UIGlobals.refresh() }, 1.0/6.0),0,0)
        amountPane.add(UtilityComponentFactory.proportionalButton("Less", EventHandler { amount = max(amount-1,0); UIGlobals.refresh() }, 1.0/6.0),1,0)
        amountPane.add(UtilityComponentFactory.shortProportionalLabel(DecimalFormat("#.##").format(amount), 1.0/3.0),2,0)
        amountPane.add(UtilityComponentFactory.proportionalButton("More", EventHandler { amount = amount+1; UIGlobals.refresh() }, 1.0/6.0),3,0)
        amountPane.add(UtilityComponentFactory.proportionalButton("50 More", EventHandler { amount = amount+50; UIGlobals.refresh() }, 1.0/6.0),4,0)

        retval.add(targetPane, 0,2)
        retval.add(amountPane,0,3)

        return retval
    }

    override fun universalDisplay(perspective: ShortStateCharacter?): Scene {
        return Scene(actionPane(this))
    }

    override fun equals(other: Any?): Boolean {
        return other is GiveResource && other.characterId == characterId && other.amount == amount
    }

    override fun collidesWith(other: Action): Boolean{
        return other is GiveResource && other.characterId == this.characterId
    }

}