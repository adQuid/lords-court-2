package ui.specialdisplayables

import game.GameSetup
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import main.Controller
import main.UIGlobals
import scenario.TutorialGameSetup
import ui.NoPerspectiveDisplayable
import ui.componentfactory.UtilityComponentFactory

class NewGameMenu: NoPerspectiveDisplayable() {

    override fun display(): Scene {
        val pane = GridPane()

        val characterDetails = UtilityComponentFactory.shortWideLabel("")

        val characterBio = UtilityComponentFactory.shortWideLabel("")
        characterBio.setMinSize(UIGlobals.totalWidth(), UIGlobals.totalHeight() * 0.5)

        val startGameButton = UtilityComponentFactory.shortWideButton("Start", EventHandler {
            Controller.singleton!!.newGame(TutorialGameSetup.setupAgricultureGame()) })
        startGameButton.isDisable = true

        pane.add(UtilityComponentFactory.shortWideLabel("Select Character"), 0, 1)

        val characterSelectPane = GridPane()
        characterSelectPane.add(UtilityComponentFactory.proportionalButton("Prince Melkar", EventHandler {
            characterDetails.text = "Age: 15   Difficulty: Easy"
            characterBio.text = "    You are Melkar, prince of Danswada. Your father, King Mayron, marched to war against the Grogan months ago, but left you behind until recently. You have now arrived at the small allied town of Port Fog, at his summons, only to find he had already departed on campaign. Without an army, you've waited in town for his return. This morning, you hear the sound of an army marching into town... \n    As the heir of a stable dynasty ruling a thriving land, Melkar has a bright future. The realm of Danswada has troubles, which will only grow worse as time goes on, but in the immediate future he has the privilage to be able to make quite a few mistakes. The start of Melkar's story also serves as a game tutorial."; startGameButton.isDisable = false }, 4.0), 0, 0)
        characterSelectPane.add(UtilityComponentFactory.proportionalButton("", EventHandler { characterDetails.text = ""; characterBio.text = ""; startGameButton.isDisable = true }, 4.0), 1, 0)
        characterSelectPane.add(UtilityComponentFactory.proportionalButton("", EventHandler { characterDetails.text = ""; characterBio.text = ""; startGameButton.isDisable = true }, 4.0), 2, 0)
        characterSelectPane.add(UtilityComponentFactory.proportionalButton("", EventHandler { characterDetails.text = ""; characterBio.text = ""; startGameButton.isDisable = true }, 4.0), 3, 0)

        pane.add(characterSelectPane,0,2)

        pane.add(characterDetails, 0,3)
        pane.add(characterBio, 0, 4)
        pane.add(startGameButton, 0, 5)
        pane.add(UtilityComponentFactory.backButton(), 0, 6)



        return Scene(pane)
    }
}