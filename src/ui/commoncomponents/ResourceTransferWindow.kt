package ui.commoncomponents

import gamelogic.resources.Resources
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import main.UIGlobals
import shortstate.ShortStateCharacter
import ui.Describable
import ui.Displayable
import ui.componentfactory.UtilityComponentFactory

class ResourceTransferWindow: Displayable {

    val res1: Resources
    val res2: Resources

    constructor(res1: Resources, res2: Resources){
        this.res1 = res1
        this.res2 = res2
    }

    override fun universalDisplay(perspective: ShortStateCharacter?): Scene {
        val pane = GridPane()

        val listPane = GridPane()
        listPane.add(UtilityComponentFactory.shortProportionalLabel("Resources 1", 0.5), 0, 0)
        listPane.add(UtilityComponentFactory.shortProportionalLabel("Resources 2", 0.5), 1, 0)

        listPane.add(UtilityComponentFactory.basicList(res1.resources.map{resource -> ResourceAmountDisplay(resource.key, resource.value)}, { display -> res1.add(display.type, -display.amount); res2.add(display.type, display.amount); UIGlobals.refresh() },null,0.5, 0.7),0,1)
        listPane.add(UtilityComponentFactory.basicList(res2.resources.map{resource -> ResourceAmountDisplay(resource.key, resource.value)}, { display -> res1.add(display.type, display.amount); res2.add(display.type, -display.amount); UIGlobals.refresh() },null,0.5, 0.7),1,1)

        pane.add(listPane, 0,0)
        pane.add(UtilityComponentFactory.shortButton("Save Changes", null), 0 ,1)
        pane.add(UtilityComponentFactory.backButton(), 0, 2)

        return Scene(pane)
    }

    class ResourceAmountDisplay: Describable {
        val type: String
        val amount: Int

        constructor(type: String, amount: Int){
            this.type = type
            this.amount = amount
        }

        override fun tooltip(perspective: ShortStateCharacter): String {
            return "${type}: ${amount}"
        }

        override fun description(): String {
            return "${type}: ${amount}"
        }

    }
}