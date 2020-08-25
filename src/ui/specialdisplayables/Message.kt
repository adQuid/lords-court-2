package ui.specialdisplayables

import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.layout.Pane
import main.UIGlobals
import ui.NoPerspectiveDisplayable
import ui.componentfactory.UtilityComponentFactory

class Message: NoPerspectiveDisplayable {

    val text: String

    constructor(text: String){
        this.text = text
    }

    override fun display(): Scene {
        val pane = Pane()

        val label = UtilityComponentFactory.proportionalLabel(text, 1.0, 1.0)

        label.onMouseClicked = EventHandler { UIGlobals.defocus() }
        pane.children.add(label)
        return Scene(pane)
    }
}