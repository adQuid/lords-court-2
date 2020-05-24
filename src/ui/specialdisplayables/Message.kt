package ui.specialdisplayables

import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.layout.Pane
import main.UIGlobals
import ui.NoPerspectiveDisplayable

class Message: NoPerspectiveDisplayable {

    val text: String

    constructor(text: String){
        this.text = text
    }

    override fun display(): Scene {
        val pane = Pane()

        val label = Label(text)
        label.setPrefSize(UIGlobals.totalWidth(), UIGlobals.totalHeight())
        label.onMouseClicked = EventHandler { UIGlobals.defocus() }
        label.alignment = Pos.CENTER
        pane.children.add(label)

        return Scene(pane)
    }
}