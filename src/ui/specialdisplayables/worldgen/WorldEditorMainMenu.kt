package ui.specialdisplayables.worldgen

import javafx.event.EventHandler
import javafx.geometry.Rectangle2D
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import main.UIGlobals
import ui.NoPerspectiveDisplayable
import ui.componentfactory.UtilityComponentFactory
import ui.specialdisplayables.MapView
import kotlin.math.max
import kotlin.math.min

object WorldEditorMainMenu: NoPerspectiveDisplayable() {

    override fun display(): Scene {
        val pane = GridPane()

        pane.add(MapView("maps/testland", 100.0,100.0).display(), 0, 0)

        val middlePane = GridPane()
        middlePane.add(UtilityComponentFactory.shortButton("Place Territory Centers", EventHandler { _ -> println("what does this do?") }), 0, 0)
        middlePane.add(UtilityComponentFactory.shortButton("Place Territory Centers", EventHandler { _ -> println("what does this do?") }), 1, 0)
        middlePane.add(UtilityComponentFactory.shortButton("Place Territory Centers", EventHandler { _ -> println("what does this do?") }), 2, 0)
        middlePane.add(UtilityComponentFactory.shortButton("Place Territory Centers", EventHandler { _ -> println("what does this do?") }), 3, 0)
        pane.add(middlePane, 0,1)

        val bottomPane = GridPane()
        bottomPane.add(UtilityComponentFactory.proportionalBackButton(2.0), 0, 0)
        bottomPane.add(UtilityComponentFactory.proportionalButton("Save Map", EventHandler { _ -> println("saving map")},2.0), 1, 0)
        pane.add(bottomPane, 0, 2)

        return Scene(pane)
    }
}