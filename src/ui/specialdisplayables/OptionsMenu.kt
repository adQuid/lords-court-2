package ui.specialdisplayables

import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.layout.GridPane
import main.Controller
import main.UIGlobals
import shortstate.ShortStateCharacter
import ui.PerspectiveDisplayable
import ui.componentfactory.UtilityComponentFactory
import ui.specialdisplayables.selectionmodal.SelectionModal
import ui.specialdisplayables.selectionmodal.Tab

class OptionsMenu: PerspectiveDisplayable() {


    override fun display(perspective: ShortStateCharacter): Scene {
        val pane = GridPane()

        val topPane = GridPane()

        val sizeUpDownPane = GridPane()
        val sizeButton1 =
            UtilityComponentFactory.proportionalButton("Size-", EventHandler { UIGlobals.GUI().rotateSize(-1) }, 0.5)
        sizeUpDownPane.add(sizeButton1, 0,0)
        val sizeButton2 =
            UtilityComponentFactory.proportionalButton("Size+", EventHandler { UIGlobals.GUI().rotateSize(1) }, 0.5)
        sizeUpDownPane.add(sizeButton2, 1,0)
        topPane.add(sizeUpDownPane, 0,0)

        val resolutionPane = GridPane()
        resolutionPane.add(setResButton(640,480),0,0)
        resolutionPane.add(setResButton(800,600),1,0)
        resolutionPane.add(setResButton(1024,780),2,0)
        resolutionPane.add(setResButton(1400,1050),3,0)
        resolutionPane.add(setResButton(1920,1080),4,0)
        resolutionPane.add(setResButton(1920,1440),5,0)
        topPane.add(resolutionPane, 0, 1)

        val saveLoadPane = GridPane()
        val saveButton = UtilityComponentFactory.proportionalButton("Save", EventHandler { UIGlobals.focusOn(
            SelectionModal("Save Game", listOf(Tab("save files", saveGames().plus("New"))), { name -> if(name == "New") { UIGlobals.focusOn(NewSaveGameMenu()) } else {Controller.singleton!!.save(name); UIGlobals.resetFocus()}})
        ) }, 0.5)
        saveLoadPane.add(saveButton, 0,2)
        val loadButton = UtilityComponentFactory.proportionalButton("Quit and Return to Main Menu", EventHandler { Controller.singleton!!.stopPlaying(); UIGlobals.focusOn(MainMenu()) }, 0.5)
        saveLoadPane.add(loadButton, 1,2)
        topPane.add(saveLoadPane,0,2)

        pane.add(topPane, 0, 0)
        pane.add(UtilityComponentFactory.shortWideButton("Filler", EventHandler {  }), 0, 3)
        pane.add(UtilityComponentFactory.shortWideButton("Filler", EventHandler {  }), 0, 4)
        pane.add(UtilityComponentFactory.shortWideButton("Filler", EventHandler {  }), 0, 5)
        pane.add(UtilityComponentFactory.shortWideButton("Filler", EventHandler {  }), 0, 6)
        pane.add(UtilityComponentFactory.shortWideButton("Filler", EventHandler {  }), 0, 7)
        pane.add(UtilityComponentFactory.shortWideButton("Filler", EventHandler {  }), 0, 8)
        pane.add(UtilityComponentFactory.backButton(), 0, 9)

        return Scene(pane)
    }

    private fun setResButton(width: Int, height: Int): Node {
        return UtilityComponentFactory.proportionalButton("${width}x${height}", EventHandler { UIGlobals.GUI().setResolution(width.toDouble(),height.toDouble()) }, 1.0/6.0)
    }
}