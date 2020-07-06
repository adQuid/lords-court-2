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
        if(scene.conversation == null){
            return bottomPane(outOfConvoButtons(scene, perspective).plus(listOf(UtilityComponentFactory.newSceneButton(perspective))), perspective)
        } else {
            return bottomPane(listOf(UtilityComponentFactory.newSceneButton(perspective)), perspective)
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
        val localActionsButton = if(scene.room.getActions(perspective).isNotEmpty()){
            UtilityComponentFactory.shortButton("Actions Here", EventHandler { _ ->
                UIGlobals.focusOn(
                    SelectionModal("Select Action",
                        roomActionButtons(scene.room, perspective),
                        { maker ->
                            maker.onClick(
                                Controller.singleton!!.shortThreadForPlayer(perspective).shortGame,
                                perspective
                            )
                        })
                )
            })
        } else {
            UtilityComponentFactory.shortButton("No Actions In this Room", null)
        }

        val endTurnButton = UtilityComponentFactory.shortButton("End Turn", EventHandler { _ -> UIGlobals.focusOn(
            EndTurnMenu()
        )})

        return listOf(localActionsButton, endTurnButton).plus(UIGlobals.activeGame().gameLogicModules.flatMap { it.bottomButtons(perspective) })
    }

    private fun roomActionButtons(room: Room, perspective: ShortStateCharacter): List<Tab<RoomActionMaker>>{
        val tab = Tab(room.name, room.getActions(perspective))

        return listOf(tab)
    }
}