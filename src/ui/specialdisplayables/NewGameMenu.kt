package ui.specialdisplayables

import game.GameSetup
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import main.Controller
import main.UIGlobals
import ui.NoPerspectiveDisplayable
import ui.componentfactory.UtilityComponentFactory

class NewGameMenu: NoPerspectiveDisplayable() {

    override fun display(): Scene {
        val pane = GridPane()

        val characterBio = UtilityComponentFactory.shortWideLabel("")
        characterBio.setMinSize(UIGlobals.totalWidth(), UIGlobals.totalHeight() * 0.6)

        val startGameButton = UtilityComponentFactory.shortWideButton("Start", EventHandler {
            Controller.singleton!!.newGame(GameSetup().setupAgricultureGame()) })
        startGameButton.isDisable = true

        pane.add(UtilityComponentFactory.shortWideLabel("Select Character"), 0, 1)

        val characterSelectPane = GridPane()
        characterSelectPane.add(UtilityComponentFactory.proportionalButton("Melkar", EventHandler {
            characterBio.text = "You are Melkar, prince of Kingdomland. This is more filler text. It's very long filler, and for a good reason. A good testing reason. Anyhow, you should go see out your father, since he's going to give you stuff because you're a spoiled rich kid and whatnot."; startGameButton.isDisable = false }, 4.0), 0, 0)
        characterSelectPane.add(UtilityComponentFactory.proportionalButton("", EventHandler { characterBio.text = ""; startGameButton.isDisable = true }, 4.0), 1, 0)
        characterSelectPane.add(UtilityComponentFactory.proportionalButton("", EventHandler { characterBio.text = ""; startGameButton.isDisable = true }, 4.0), 2, 0)
        characterSelectPane.add(UtilityComponentFactory.proportionalButton("", EventHandler { characterBio.text = ""; startGameButton.isDisable = true }, 4.0), 3, 0)

        pane.add(characterSelectPane,0,2)

        pane.add(characterBio, 0, 3)
        pane.add(startGameButton, 0, 4)
        pane.add(UtilityComponentFactory.backButton(), 0, 5)



        return Scene(pane)
    }
}