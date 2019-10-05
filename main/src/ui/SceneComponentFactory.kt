package ui

import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.layout.FlowPane
import javafx.scene.layout.GridPane
import javafx.scene.layout.Pane

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
        val btn2 = parent.generalComponents.makeTallButton("Go Somewhere Else", EventHandler { _ -> parent.focusOn(RoomSelectModal(parent, {room -> parent.focusOn(room)}) )})
        val btn3 = parent.generalComponents.makeTallButton("Filler 2", null)
        bottomButtonsPanel.children.add(btn1)
        bottomButtonsPanel.children.add(btn2)
        bottomButtonsPanel.children.add(btn3)
        return bottomButtonsPanel
    }

}