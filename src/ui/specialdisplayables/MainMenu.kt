package ui.specialdisplayables

import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import main.Controller
import main.UIGlobals
import shortstate.ShortStateCharacter
import shortstate.room.action.GoToBed
import ui.Displayable
import ui.componentfactory.UtilityComponentFactory

class MainMenu: Displayable {

    override fun display(perspective: ShortStateCharacter): Scene {
        val pane = GridPane()

        pane.add(UtilityComponentFactory.shortWideLabel("Welcome...To Lords Court"), 0, 2)
        pane.add(UtilityComponentFactory.shortWideLabel("ba"), 0, 3)
        pane.add(UtilityComponentFactory.shortWideLabel("ba da ba ba ba ba da"), 0, 4)
        pane.add(UtilityComponentFactory.shortWideLabel("ba ba ba ba ba ba bahhhhh"), 0, 5)
        pane.add(UtilityComponentFactory.shortWideLabel("babababababababababa"), 0, 6)
        pane.add(UtilityComponentFactory.shortWideLabel("Filler"), 0, 7)
        pane.add(UtilityComponentFactory.shortWideLabel("Filler"), 0, 8)
        pane.add(UtilityComponentFactory.shortWideLabel("Filler"), 0, 9)
        pane.add(UtilityComponentFactory.shortWideLabel("MAP EDITOR"), 0, 10)
        pane.add(UtilityComponentFactory.shortWideButton("PLAY", EventHandler { UIGlobals.resetFocus() }), 0, 11)


        return Scene(pane)
    }
}