package ui.commoncomponents

import gamelogic.resources.Resources
import javafx.event.EventHandler
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
    val callback: (res1: Resources, res2: Resources) -> Unit

    var transferAmount = 1

    constructor(res1: Resources, res2: Resources, callback: (res1: Resources, res2: Resources) -> Unit){
        this.res1 = res1
        this.res2 = res2
        this.callback = callback
    }

    override fun universalDisplay(perspective: ShortStateCharacter?): Scene {
        val pane = GridPane()

        val listPane = GridPane()
        listPane.add(UtilityComponentFactory.shortProportionalLabel("Resources 1", 0.5), 0, 0)
        listPane.add(UtilityComponentFactory.shortProportionalLabel("Resources 2", 0.5), 1, 0)

        listPane.add(UtilityComponentFactory.basicList(res1.resources.map{resource -> ResourceAmountDisplay(resource.key, resource.value)}, { display ->  transferResources(display, res1, res2)},null,0.5, 0.7),0,1)
        listPane.add(UtilityComponentFactory.basicList(res2.resources.map{resource -> ResourceAmountDisplay(resource.key, resource.value)}, { display -> transferResources(display, res2, res1) },null,0.5, 0.7),1,1)

        pane.add(listPane, 0,0)

        val amountPane = GridPane()
        amountPane.add(UtilityComponentFactory.proportionalButton("1", EventHandler{transferAmount = 1}, 0.2), 0, 0)
        amountPane.add(UtilityComponentFactory.proportionalButton("5", EventHandler{transferAmount = 5}, 0.2), 1, 0)
        amountPane.add(UtilityComponentFactory.proportionalButton("10", EventHandler{transferAmount = 10}, 0.2), 2, 0)
        amountPane.add(UtilityComponentFactory.proportionalButton("50", EventHandler{transferAmount = 50}, 0.2), 3, 0)
        amountPane.add(UtilityComponentFactory.proportionalButton("100", EventHandler{transferAmount = 100}, 0.2), 4, 0)
        pane.add(amountPane, 0 , 1)

        val resolutionPane = GridPane()
        resolutionPane.add(UtilityComponentFactory.shortButton("Save Changes", EventHandler { callback(res1, res2); UIGlobals.defocus()}), 0 ,1)
        resolutionPane.add(UtilityComponentFactory.backButton(), 0, 2)
        pane.add(resolutionPane, 0 , 2)

        return Scene(pane)
    }

    fun transferResources(display: ResourceAmountDisplay, from: Resources, to: Resources){
        if(from.get(display.type) >= transferAmount){
            from.add(display.type, -transferAmount)
            to.add(display.type, transferAmount)
        }
        UIGlobals.refresh()
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