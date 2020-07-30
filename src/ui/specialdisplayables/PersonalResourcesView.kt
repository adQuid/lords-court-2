package ui.specialdisplayables

import gamelogic.playerresources.PlayerResourceTypes
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import shortstate.ShortStateCharacter
import ui.PerspectiveDisplayable
import ui.componentfactory.UtilityComponentFactory

class PersonalResourcesView: PerspectiveDisplayable() {
    override fun display(perspective: ShortStateCharacter): Scene {
        val pane = GridPane()

        pane.add(UtilityComponentFactory.shortWideLabel("Gold: ${perspective.player.resources.get(PlayerResourceTypes.GOLD_NAME)}"), 0, 2)
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