package ui.specialdisplayables

import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import main.Controller
import main.UIGlobals
import shortstate.ShortStateCharacter
import ui.NoPerspectiveDisplayable
import ui.PerspectiveDisplayable
import ui.componentfactory.UtilityComponentFactory
import ui.specialdisplayables.worldgen.WorldEditorMainMenu

class MainMenu: NoPerspectiveDisplayable() {

    override fun display(): Scene {
        val pane = GridPane()

        pane.add(UtilityComponentFactory.shortWideLabel("Welcome...To Lords Court"), 0, 2)
        pane.add(UtilityComponentFactory.shortWideLabel("ba"), 0, 3)
        pane.add(UtilityComponentFactory.shortWideLabel("ba da ba ba ba ba da"), 0, 4)
        pane.add(UtilityComponentFactory.shortWideLabel("ba ba ba ba ba ba bahhhhh"), 0, 5)
        pane.add(UtilityComponentFactory.shortWideLabel("babababababababababa"), 0, 6)
        pane.add(UtilityComponentFactory.shortWideLabel("Filler"), 0, 7)
        pane.add(UtilityComponentFactory.shortWideLabel("Filler"), 0, 8)
        pane.add(UtilityComponentFactory.shortWideLabel("Filler"), 0, 9)
        pane.add(UtilityComponentFactory.shortWideButton("MAP EDITOR",  EventHandler { UIGlobals.focusOn(WorldEditorMainMenu()) }), 0, 10)
        pane.add(UtilityComponentFactory.shortWideButton("PLAY", EventHandler {
            Controller.singleton!!.newGame() }), 0, 11)


        return Scene(pane)
    }
}