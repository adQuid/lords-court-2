package gamelogic.government.actionTypes

import game.action.Action
import game.Game
import game.GameCharacter
import game.action.GlobalActionTypeFactory
import gamelogic.government.Capital
import gamelogic.government.Count
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

class GiveTerritory: Action {

    companion object{
        val typeName: String
            get() = "GiveTerritory"
    }
    val terId: Int
    var target: Int

    constructor(id: Int, target: Int){
        this.terId = id
        this.target = target
    }

    override fun doAction(game: Game, player: GameCharacter){
        val titleToTransfer = game.titles.filter { it is Count && it.capital.territory!!.id == terId }.firstOrNull()
        if(titleToTransfer != null ){
            game.transferTitle(player, game.characterById(target), titleToTransfer)
        }
    }

    override fun tooltip(perspective: ShortStateCharacter): String {
        return "give control of ${terId} to ${target}"
    }

    override fun saveString(): Map<String, Any> {
        return hashMapOf(
            GlobalActionTypeFactory.TYPE_NAME to typeName,
            "target" to target,
            "territory" to terId
        )
    }

    override fun description(): String {
        return "Grant control of ${capitalById().territory!!.name} to ${characterById()}. This will take place at the start of next turn."
    }

    private fun capitalById(): Capital {
        val logic = UIGlobals.activeGame().moduleOfType(GovernmentLogicModule.type) as GovernmentLogicModule
        return logic.capitalById(terId)
    }

    private fun characterById(): GameCharacter {
        return UIGlobals.activeGame().characterById(target)
    }

    override fun universalDisplay(perspective: ShortStateCharacter?): Scene {
        return Scene(actionPane(this, null))
    }

    override fun equals(other: Any?): Boolean {
        return other is GiveTerritory && other.terId == terId && other.target == target
    }

    override fun collidesWith(other: Action): Boolean{
        return other is GiveTerritory && other.terId == this.terId
    }
}