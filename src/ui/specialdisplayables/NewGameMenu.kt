package ui.specialdisplayables

import com.sun.webkit.network.Util
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.layout.GridPane
import main.Controller
import main.UIGlobals
import scenario.tutorial.TutorialGameSetup
import scenario.tutorial.TutorialGameSetup.TUTORIAL_PLAYER_NAME
import ui.NoPerspectiveDisplayable
import ui.componentfactory.UtilityComponentFactory

class NewGameMenu: NoPerspectiveDisplayable() {

    override fun display(): Scene {
        val pane = GridPane()

        val characterDetails = UtilityComponentFactory.shortWideLabel("")

        val characterBio = UtilityComponentFactory.proportionalLabel("", 1.0 ,0.5)

        val startGameButton = UtilityComponentFactory.shortWideButton("Start",
            EventHandler {
                Platform.runLater {
                    Controller.singleton!!.newGame(TutorialGameSetup.setupAgricultureGame())
                }
            })
        UtilityComponentFactory.setButtonDisable(startGameButton, true)

        pane.add(UtilityComponentFactory.shortWideLabel("Select Character"), 0, 1)

        val characterSelectPane = GridPane()
        characterSelectPane.add(UtilityComponentFactory.proportionalButton("Prince ${TUTORIAL_PLAYER_NAME}", EventHandler {
            (characterDetails.children[1] as Label).text = "Age: 15   Difficulty: Easy"
            (characterBio.children[1] as Label).text = "    You are ${TUTORIAL_PLAYER_NAME}, prince of Danswada. Your father, King Mayron, marched to war against the Grogan months ago, but left you behind until recently. You have now arrived at the small allied town of Port Fog, at his summons, only to find he had already departed on campaign. Without an army, you've waited in town for his return. This morning, you hear the sound of an army marching into town... \n    As the heir of a stable dynasty ruling a thriving land, ${TUTORIAL_PLAYER_NAME} has a bright future. The realm of Danswada has troubles, which will only grow worse as time goes on, but in the immediate future he has the privilage to be able to make quite a few mistakes. The start of ${TUTORIAL_PLAYER_NAME}'s story also serves as a game tutorial."; startGameButton.isDisable = false
            UtilityComponentFactory.setButtonDisable(startGameButton, false)
        }
            , 0.25), 0, 0)
        characterSelectPane.add(UtilityComponentFactory.proportionalButton("", EventHandler { (characterDetails.children[1] as Label).text = ""; (characterBio.children[1] as Label).text = ""; startGameButton.isDisable = true; UtilityComponentFactory.setButtonDisable(startGameButton, true)}, 4.0), 1, 0)
        characterSelectPane.add(UtilityComponentFactory.proportionalButton("", EventHandler { (characterDetails.children[1] as Label).text = ""; (characterBio.children[1] as Label).text = ""; startGameButton.isDisable = true; UtilityComponentFactory.setButtonDisable(startGameButton, true) }, 4.0), 2, 0)
        characterSelectPane.add(UtilityComponentFactory.proportionalButton("", EventHandler { (characterDetails.children[1] as Label).text = ""; (characterBio.children[1] as Label).text = ""; startGameButton.isDisable = true; UtilityComponentFactory.setButtonDisable(startGameButton, true) }, 4.0), 3, 0)

        pane.add(characterSelectPane,0,2)

        pane.add(characterDetails, 0,3)
        pane.add(characterBio, 0, 4)
        pane.add(startGameButton, 0, 5)
        pane.add(UtilityComponentFactory.backButton(), 0, 6)



        return Scene(pane)
    }
}