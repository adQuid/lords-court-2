package ui.specialdisplayables

import game.GameSetup
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import main.Controller
import ui.NoPerspectiveDisplayable
import ui.componentfactory.UtilityComponentFactory

class NewGameMenu: NoPerspectiveDisplayable() {

    override fun display(): Scene {
        val pane = GridPane()

        pane.add(UtilityComponentFactory.shortWideLabel("New Game"), 0, 2)
        pane.add(UtilityComponentFactory.shortWideLabel("filler"), 0, 3)
        pane.add(UtilityComponentFactory.shortWideLabel("filler"), 0, 4)
        pane.add(UtilityComponentFactory.shortWideLabel("filler"), 0, 5)
        pane.add(UtilityComponentFactory.shortWideLabel("filler"), 0, 6)
        pane.add(UtilityComponentFactory.shortWideLabel("Filler"), 0, 7)
        pane.add(UtilityComponentFactory.shortWideLabel("Filler"), 0, 8)
        pane.add(UtilityComponentFactory.shortWideLabel("Filler"), 0, 9)
        pane.add(UtilityComponentFactory.shortWideButton("Cookie World",  EventHandler { Controller.singleton!!.newGame(GameSetup().setupCookieworld()) }), 0, 10)
        pane.add(UtilityComponentFactory.shortWideButton("Standard Game", EventHandler {
            Controller.singleton!!.newGame(GameSetup().setupAgricultureGame()) }), 0, 11)


        return Scene(pane)
    }
}