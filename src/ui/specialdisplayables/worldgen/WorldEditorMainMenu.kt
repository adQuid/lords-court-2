package ui.specialdisplayables.worldgen

import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import shortstate.ShortStateCharacter
import ui.NoPerspectiveDisplayable
import ui.PerspectiveDisplayable
import ui.componentfactory.UtilityComponentFactory

class WorldEditorMainMenu: NoPerspectiveDisplayable() {

    override fun display(): Scene {
        val pane = GridPane()

        pane.add(UtilityComponentFactory.imageView("maps/testland/background.png"), 0, 0)

        val bottomPane = GridPane()
        bottomPane.add(UtilityComponentFactory.proportionalBackButton(2.0), 0, 0)
        bottomPane.add(UtilityComponentFactory.proportionalButton("Save Map", EventHandler { _ -> println("saving map")},2.0), 1, 0)

        pane.add(bottomPane, 0, 11)

        return Scene(pane)
    }
}