package ui

import action.Action
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.layout.GridPane

class ActionSelectModal {

    val parent: MainUI
    val closeAction: (Action) -> Unit

    constructor(parent: MainUI, action: (Action) -> Unit){
        this.parent = parent
        this.closeAction = action
    }

    fun getScene(): Scene {
        val pane = GridPane()
        val btn1 = parent.generalComponents.makeShortButton("Action", EventHandler { _ -> println("yup that works") })
        pane.add(btn1, 0, 0)
        val btn2 = parent.generalComponents.makeShortButton( "filler", null)
        pane.add(btn2, 1, 0)
        val btn3 = parent.generalComponents.makeShortButton("filler", null)
        pane.add(btn3, 2, 0)
        val btn4 = parent.generalComponents.makeShortButton("filler", null)
        pane.add(btn4, 3, 0)
        val btn5 = parent.generalComponents.makeShortButton("filler", null)
        pane.add(btn5, 0, 1)
        val btn6 = parent.generalComponents.makeShortButton("filler", null)
        pane.add(btn6, 1, 1)
        val btn7 = parent.generalComponents.makeShortButton("filler", null)
        pane.add(btn7, 2, 1)
        val btn8 = parent.generalComponents.makeShortButton(
            "Cancel",
            EventHandler { parent.setFocus(parent.conversation); parent.display() })
        pane.add(btn8, 3, 1)

        return Scene(pane, parent.totalWidth, parent.totalHeight)
    }


}