package gamelogic.government.specialdisplayables

import aibrain.UnfinishedDeal
import game.GameCharacter
import gamelogic.government.GovernmentLogicModule
import gamelogic.government.actionTypes.SetTaxRate
import gamelogic.resources.ResourceTypes
import gamelogic.territory.Territory
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import main.UIGlobals
import shortstate.ShortStateCharacter
import ui.Displayable
import ui.componentfactory.UtilityComponentFactory
import ui.specialdisplayables.contructorobjects.WritConstructor
import ui.specialdisplayables.selectionmodal.SelectionModal
import ui.specialdisplayables.selectionmodal.Tab

class LawsView: Displayable {

    val terId: Int

    constructor(terId: Int){
        this.terId = terId
    }

    override fun universalDisplay(perspective: ShortStateCharacter?): Scene {
        val capital = (UIGlobals.activeGame().moduleOfType(GovernmentLogicModule.type) as GovernmentLogicModule).capitalById(terId)

        val pane = GridPane()

        val taxesPane = GridPane()
        taxesPane.add(UtilityComponentFactory.shortProportionalLabel("Income Tax (Flour): "+capital.taxes[ResourceTypes.FLOUR_NAME], 2.0), 0, 0)
        taxesPane.add(UtilityComponentFactory.proportionalButton("Change", EventHandler { makeWritFromAction(perspective!!) },0.25),1,0)
        pane.add(taxesPane,0,0)

        pane.add(UtilityComponentFactory.shortWideButton("Laws", EventHandler { UIGlobals.focusOn(SelectionModal("Laws",listOf(
            Tab("Laws", capital.laws().toList())
        ), { UIGlobals.focusOn(it.constructorComponentFactory(capital))})) }), 0, 1)
        pane.add(UtilityComponentFactory.proportionalLabel("", 1.0, 0.6), 0, 2)
        pane.add(UtilityComponentFactory.shortWideLabel(""), 0, 3)

        pane.add(UtilityComponentFactory.backButton(), 0, 4)


        return Scene(pane)
    }

    private fun makeWritFromAction(perspective: ShortStateCharacter){
        val newAction = SetTaxRate(terId, GovernmentLogicModule.capitalById(UIGlobals.activeGame(), terId).taxes[ResourceTypes.FLOUR_NAME]!!)
        val newDeal = UnfinishedDeal(mapOf(perspective.player to setOf(newAction)))

        UIGlobals.focusOn(WritConstructor(newDeal))
        UIGlobals.focusOn(newAction)
    }
}