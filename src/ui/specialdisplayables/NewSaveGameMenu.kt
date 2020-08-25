package ui.specialdisplayables

import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import main.Controller
import main.UIGlobals
import shortstate.ShortStateCharacter
import shortstate.room.action.GoToBed
import ui.PerspectiveDisplayable
import ui.componentfactory.UtilityComponentFactory

class NewSaveGameMenu: PerspectiveDisplayable() {


    override fun display(perspective: ShortStateCharacter): Scene {
        val pane = GridPane()

        val textField = UtilityComponentFactory.shortWideTextField("")

        pane.add(textField, 0, 0)

        pane.add(UtilityComponentFactory.shortWideLabel("Filler"),0,1)
        pane.add(UtilityComponentFactory.shortWideLabel("Filler"),0,2)
        pane.add(UtilityComponentFactory.shortWideLabel("Filler"),0,3)
        pane.add(UtilityComponentFactory.shortWideLabel("Filler"),0,4)
        pane.add(UtilityComponentFactory.shortWideLabel("Filler"),0,5)
        pane.add(UtilityComponentFactory.shortWideLabel("Filler"),0,6)
        pane.add(UtilityComponentFactory.shortWideLabel("Filler"),0,7)
        pane.add(UtilityComponentFactory.shortWideLabel("Filler"),0,8)

        val bottomPane = GridPane()
        bottomPane.add(UtilityComponentFactory.proportionalBackButton(2.0), 0, 0)
        bottomPane.add(UtilityComponentFactory.proportionalButton("Save Game", EventHandler { _ -> Controller.singleton!!.save(textField.text); UIGlobals.defocus(); UIGlobals.resetFocus()},0.5), 1, 0)
        pane.add(bottomPane, 0, 9)

        return Scene(pane)
    }
}