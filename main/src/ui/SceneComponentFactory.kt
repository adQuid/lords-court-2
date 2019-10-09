package ui

import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.layout.GridPane
import javafx.scene.layout.Pane
import shortstate.scenemaker.ConversationMaker
import shortstate.scenemaker.GoToRoomSoloMaker
import shortstate.scenemaker.SceneMaker
import ui.selectionmodal.SelectionModal
import ui.selectionmodal.Tab

class SceneComponentFactory {

    val parent: MainUI

    constructor(parent: MainUI){
        this.parent = parent
    }

    fun scenePage(): Scene {
        if(parent.scene!!.conversation != null){
            return parent.conversationComponents.inConvoPage()
        } else {
            return outOfConvoPage()
        }
    }

    private fun outOfConvoPage(): Scene {
        val root = GridPane()
        root.add(outOfConvoButtons(), 0, 1)
        root.add(parent.generalComponents.sceneImage(), 0, 0)
        val scene = Scene(root, parent.totalWidth, parent.totalHeight)
        return scene
    }

    private fun outOfConvoButtons(): Pane {
        val btn1 = parent.generalComponents.makeShortButton("Personal Actions", null)
        val btn2 = newSceneButton()
        val btn3 = parent.generalComponents.makeShortButton("Filler 2", null)
        return parent.generalComponents.makeBottomPane(listOf(btn1,btn2,btn3))
    }

    fun newSceneButton(): Button {
        return parent.generalComponents.makeShortButton("Go Somewhere Else",
            EventHandler { _ -> parent.focusOn(
                SelectionModal(parent, newSceneButtons(), {maker -> goToNewSceneIfApplicable(maker)})
            )
            }
        )
    }

    private fun newSceneButtons(): List<Tab<SceneMaker>>{
        val goToRoomMakers = parent.playingAs().player.location.rooms.map { room -> GoToRoomSoloMaker(parent.playingAs(), room) }
        val goToRoomTab = Tab<SceneMaker>("Go to Room", goToRoomMakers)

        val conversationMakers = parent.shortGame().players.minusElement(parent.playingAs())
            .map { player -> ConversationMaker(parent.playingAs(), player, parent.playingAs().player.location.rooms[0]) }
        val conversationTab = Tab<SceneMaker>("Conversation", conversationMakers)

        return listOf(goToRoomTab, conversationTab)
    }

    private fun goToNewSceneIfApplicable(maker: SceneMaker){
        val scene = parent.shortGame().addScene(maker)
        if(scene != null){
            parent.focusOn(scene)
        }
    }

}