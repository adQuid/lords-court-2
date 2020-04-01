package ui.specialdisplayables

import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import main.Controller
import main.UIGlobals
import shortstate.ShortStateCharacter
import ui.Displayable
import ui.componentfactory.UtilityComponentFactory

class OptionsMenu: Displayable {


    override fun display(perspective: ShortStateCharacter): Scene {
        val pane = GridPane()

        val topPane = GridPane()
        val sizeButton1 =
            UtilityComponentFactory.shortButton("Size-", EventHandler { UIGlobals.GUI().rotateSize(-1) })
        topPane.add(sizeButton1, 0,0)

        val sizeButton2 =
            UtilityComponentFactory.shortButton("Size+", EventHandler { UIGlobals.GUI().rotateSize(1) })
        topPane.add(sizeButton2, 1,0)

        val saveButton = UtilityComponentFactory.shortButton("Save", EventHandler { Controller.singleton!!.save() })
        topPane.add(saveButton, 0,1)

        val loadButton = UtilityComponentFactory.shortButton("Load", EventHandler { Controller.singleton!!.load(); UIGlobals.resetFocus() })
        topPane.add(loadButton, 1,1)

        pane.add(topPane, 0, 0)
        pane.add(UtilityComponentFactory.shortWideButton("Filler", EventHandler {  }), 0, 2)
        pane.add(UtilityComponentFactory.shortWideButton("Filler", EventHandler {  }), 0, 3)
        pane.add(UtilityComponentFactory.shortWideButton("Filler", EventHandler {  }), 0, 4)
        pane.add(UtilityComponentFactory.shortWideButton("Filler", EventHandler {  }), 0, 5)
        pane.add(UtilityComponentFactory.shortWideButton("Filler", EventHandler {  }), 0, 6)
        pane.add(UtilityComponentFactory.shortWideButton("Filler", EventHandler {  }), 0, 7)
        pane.add(UtilityComponentFactory.shortWideButton("Filler", EventHandler {  }), 0, 8)
        pane.add(UtilityComponentFactory.shortWideButton("Filler", EventHandler {  }), 0, 9)
        pane.add(UtilityComponentFactory.shortWideButton("Filler", EventHandler {  }), 0, 10)
        pane.add(UtilityComponentFactory.backButton(), 0, 11)

        return Scene(pane)
    }
}