package ui

import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.layout.FlowPane
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
        val bottomButtonsPanel = FlowPane()
        val btn1 = parent.generalComponents.makeTallButton("Personal Actions", null)
        val btn2 = parent.generalComponents.makeTall(newSceneButton())
        val btn3 = parent.generalComponents.makeTallButton("Filler 2", null)
        bottomButtonsPanel.children.add(btn1)
        bottomButtonsPanel.children.add(btn2)
        bottomButtonsPanel.children.add(btn3)
        return bottomButtonsPanel
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