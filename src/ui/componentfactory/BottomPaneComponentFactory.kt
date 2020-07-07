package ui.componentfactory

import gamelogicmodules.capital.specialdisplayables.TravelView
import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.layout.GridPane
import javafx.scene.layout.Pane
import main.Controller
import main.UIGlobals
import shortstate.ShortGameScene
import shortstate.ShortStateCharacter
import shortstate.room.Room
import shortstate.room.RoomActionMaker
import ui.specialdisplayables.EndTurnMenu
import ui.specialdisplayables.selectionmodal.SelectionModal
import ui.specialdisplayables.selectionmodal.Tab

object BottomPaneComponentFactory {

    fun sceneBottomPane(scene: ShortGameScene, perspective: ShortStateCharacter): Pane {
        return bottomPane(listOf(UtilityComponentFactory.newSceneButton(perspective)), perspective)
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
}