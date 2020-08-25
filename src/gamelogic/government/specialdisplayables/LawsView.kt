package gamelogic.government.specialdisplayables

import aibrain.UnfinishedDeal
import game.GameCharacter
import gamelogic.government.GovernmentLogicModule
import gamelogic.government.actionTypes.SetTaxRate
import gamelogic.territory.Territory
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import main.UIGlobals
import shortstate.ShortStateCharacter
import ui.Displayable
import ui.componentfactory.UtilityComponentFactory
import ui.specialdisplayables.contructorobjects.WritConstructor

class LawsView: Displayable {

    val terId: Int

    constructor(terId: Int){
        this.terId = terId
    }

    override fun universalDisplay(perspective: ShortStateCharacter?): Scene {
        val capital = (UIGlobals.activeGame().moduleOfType(GovernmentLogicModule.type) as GovernmentLogicModule).capitalById(terId)

        val pane = GridPane()

        val taxesPane = GridPane()
        taxesPane.add(UtilityComponentFactory.shortProportionalLabel("Income Tax (Flour): "+capital.taxes[Territory.FLOUR_NAME], 2.0), 0, 0)
        taxesPane.add(UtilityComponentFactory.proportionalButton("Change", EventHandler { makeWritFromAction(perspective!!) },0.25),1,0)
        pane.add(taxesPane,0,0)

        pane.add(UtilityComponentFactory.shortWideLabel(""), 0, 1)
        pane.add(UtilityComponentFactory.shortWideLabel(""), 0, 2)
        pane.add(UtilityComponentFactory.shortWideLabel(""), 0, 3)
        pane.add(UtilityComponentFactory.shortWideLabel(""), 0, 4)
        pane.add(UtilityComponentFactory.shortWideLabel(""), 0, 5)
        pane.add(UtilityComponentFactory.shortWideLabel(""), 0, 6)
        pane.add(UtilityComponentFactory.shortWideLabel(""), 0, 7)
        pane.add(UtilityComponentFactory.shortWideLabel(""), 0, 8)

        pane.add(UtilityComponentFactory.backButton(), 0, 9)


        return Scene(pane)
    }

    private fun makeWritFromAction(perspective: ShortStateCharacter){
        val newAction = SetTaxRate(terId, GovernmentLogicModule.capitalById(UIGlobals.activeGame(), terId).taxes[Territory.FLOUR_NAME]!!)
        val newDeal = UnfinishedDeal(mapOf(perspective.player to setOf(newAction)))

        UIGlobals.focusOn(WritConstructor(newDeal))
        UIGlobals.focusOn(newAction)
    }

    private fun writFromLawChange(player: GameCharacter): UnfinishedDeal{
        return UnfinishedDeal(mapOf(player to setOf()))
    }
}