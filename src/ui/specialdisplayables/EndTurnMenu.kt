package ui.specialdisplayables

import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import main.Controller
import shortstate.ShortStateCharacter
import shortstate.room.action.GoToBed
import ui.PerspectiveDisplayable
import ui.componentfactory.UtilityComponentFactory

class EndTurnMenu: PerspectiveDisplayable() {


    override fun display(perspective: ShortStateCharacter): Scene {
        val pane = GridPane()

        pane.add(UtilityComponentFactory.shortWideLabel("Remaining Energy: ${perspective.energy}"), 0, 2)
        pane.add(UtilityComponentFactory.shortWideButton("Filler", EventHandler {  }), 0, 3)
        pane.add(UtilityComponentFactory.shortWideButton("Filler", EventHandler {  }), 0, 4)
        pane.add(UtilityComponentFactory.shortWideButton("Filler", EventHandler {  }), 0, 5)
        pane.add(UtilityComponentFactory.shortWideButton("Filler", EventHandler {  }), 0, 6)
        pane.add(UtilityComponentFactory.shortWideButton("Filler", EventHandler {  }), 0, 7)
        pane.add(UtilityComponentFactory.shortWideButton("Filler", EventHandler {  }), 0, 8)
        pane.add(UtilityComponentFactory.shortWideButton("Filler", EventHandler {  }), 0, 9)
        pane.add(UtilityComponentFactory.shortWideButton("Filler", EventHandler {  }), 0, 10)

        val bottomPane = GridPane()
        bottomPane.add(UtilityComponentFactory.proportionalBackButton(2.0), 0, 0)
        bottomPane.add(UtilityComponentFactory.proportionalButton("End Turn", EventHandler { _ -> GoToBed().doAction(Controller.singleton!!.shortThreadForPlayer(perspective).shortGame, perspective)},2.0), 1, 0)

        pane.add(bottomPane, 0, 11)

        return Scene(pane)
    }
}