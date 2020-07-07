package ui.componentfactory

import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.layout.GridPane
import javafx.scene.layout.Pane
import main.UIGlobals
import shortstate.ShortGameScene
import shortstate.ShortStateCharacter
import ui.specialdisplayables.EndTurnMenu

object BottomPaneComponentFactory {

    fun sceneBottomPane(scene: ShortGameScene, perspective: ShortStateCharacter): Pane {

        if(scene.conversation == null){
            return bottomPane(listOf(UtilityComponentFactory.newSceneButton(perspective)).plus(outOfConvoButtons(scene, perspective)), perspective)
        } else {
            return bottomPane(listOf(), perspective)
        }
    }

    fun bottomPane(buttons: List<Button>, perspective: ShortStateCharacter): Pane {

        val buttonsPane = GridPane()
        for(index in 0..3){
            if(index < buttons.size){
                buttonsPane.add(buttons[index], (index % 4), if(index > 3) 1 else 0)
            } else {
                buttonsPane.add(UtilityComponentFactory.shortButton("", null), (index % 4), if(index > 3) 1 else 0)
            }
        }

        val retval = GridPane()
        retval.add(buttonsPane, 0,1)

        return retval
    }

    fun outOfConvoButtons(scene: ShortGameScene, perspective: ShortStateCharacter): List<Button> {
        val endTurnButton = UtilityComponentFactory.shortButton("End Turn", EventHandler { _ -> UIGlobals.focusOn(
            EndTurnMenu()
        )})

        return listOf(endTurnButton).plus(UIGlobals.activeGame().gameLogicModules.flatMap { it.bottomButtons(perspective) })
    }
}