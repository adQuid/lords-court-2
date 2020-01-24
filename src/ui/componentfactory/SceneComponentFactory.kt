package ui.componentfactory

import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.layout.GridPane
import javafx.scene.layout.Pane
import shortstate.report.Report
import shortstate.room.Room
import shortstate.room.RoomAction
import ui.MainUI
import ui.selectionmodal.SelectionModal
import ui.selectionmodal.Tab

class SceneComponentFactory {

    val parent: MainUI

    constructor(parent: MainUI){
        this.parent = parent
    }

    fun scenePage(): Scene {
        if(parent.shortGameScene!!.conversation != null){
            return parent.conversationComponents.inConvoPage()
        } else {
            return outOfConvoPage()
        }
    }

    private fun outOfConvoPage(): Scene {
        val root = GridPane()
        root.add(outOfConvoButtons(), 0, 1)
        root.add(parent.utilityComponents.sceneImage(), 0, 0)
        val scene = Scene(root, parent.totalWidth, parent.totalHeight)
        return scene
    }

    private fun outOfConvoButtons(): Pane {
        val btn1 = parent.utilityComponents.shortButton("Personal Actions", null)
        val btn2 = if(parent.shortGameScene!!.room.getActions(parent.playingAs().player).isNotEmpty()){
            parent.utilityComponents.shortButton("Actions Here", EventHandler { _ ->
                parent.focusOn(SelectionModal(parent, roomActionButtons(parent.shortGameScene!!.room), { action -> action.doAction(parent.shortThread(), parent.playingAs()); parent.refocus()}))
            })
        } else {
            parent.utilityComponents.shortButton("No Actions In this Room", null)
        }
        val btn3 = parent.utilityComponents.newSceneButton()
        val btn4 = viewReportsButton()
        return parent.utilityComponents.bottomPane(listOf(btn1,btn2,btn3,btn4))
    }

    private fun viewReportsButton(): Button {
        return parent.utilityComponents.shortButton("View Reports",
                EventHandler { _ -> parent.focusOn(
                    SelectionModal(parent, reports(), { report -> println(report)})
                )
            }
        )
    }

    fun reports(): List<Tab<Report>>{
        val reportOptions = parent.playingAs().knownReports.map{ report -> report }
        val reportTab = Tab<Report>("Reports", reportOptions)

        return listOf(reportTab)
    }

    private fun roomActionButtons(room: Room): List<Tab<RoomAction>>{
        val tab = Tab<RoomAction>(room.name, room.getActions(parent.playingAs().player))

        return listOf(tab)
    }
}