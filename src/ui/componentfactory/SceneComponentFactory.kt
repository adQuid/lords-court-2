package ui.componentfactory

import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.image.ImageView
import javafx.scene.layout.GridPane
import javafx.scene.layout.Pane
import shortstate.ShortStateCharacter
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

    fun scenePage(perspective: ShortStateCharacter): Scene {
        if(parent.shortGameScene!!.conversation != null){
            return parent.shortGameScene!!.conversation!!.display(perspective)
        } else {
            return outOfConvoPage(perspective)
        }
    }

    fun sceneImage(): Pane {
        val imagePane = Pane()
        val backgroundView: ImageView
        if(parent.shortGameScene?.characters!!.size > 1){
            val otherPlayer = parent.shortGameScene!!.conversation!!.otherParticipant(parent.playingAs())
            backgroundView = UtilityComponentFactory.imageView(parent.shortGameScene!!.room.pictureText)
            val characterView = UtilityComponentFactory.imageView(otherPlayer.player.pictureString)
            characterView.setOnMouseClicked { event -> parent.focusOn(otherPlayer) }
            imagePane.children.addAll(backgroundView, characterView)
        } else {
            backgroundView = UtilityComponentFactory.imageView(parent.shortGameScene!!.room.pictureText)
            imagePane.children.addAll(backgroundView)
        }
        return imagePane
    }

    private fun outOfConvoPage(perspective: ShortStateCharacter): Scene {
        val root = GridPane()
        root.add(outOfConvoButtons(perspective), 0, 1)
        root.add(parent.sceneComponents.sceneImage(), 0, 0)
        val scene = Scene(root, parent.totalWidth, parent.totalHeight)
        return scene
    }

    private fun outOfConvoButtons(perspective: ShortStateCharacter): Pane {
        val btn1 = UtilityComponentFactory.shortButton("Personal Actions", null)
        val btn2 = if(parent.shortGameScene!!.room.getActions(parent.playingAs().player).isNotEmpty()){
            UtilityComponentFactory.shortButton("Actions Here", EventHandler { _ ->
                parent.focusOn(SelectionModal(parent, roomActionButtons(parent.shortGameScene!!.room), { action -> action.doAction(parent.shortThread(), parent.playingAs()); parent.refocus()}))
            })
        } else {
            UtilityComponentFactory.shortButton("No Actions In this Room", null)
        }
        val btn3 = UtilityComponentFactory.newSceneButton(perspective)
        val btn4 = viewReportsButton()
        return UtilityComponentFactory.bottomPane(listOf(btn1,btn2,btn3,btn4), perspective)
    }

    private fun viewReportsButton(): Button {
        return UtilityComponentFactory.shortButton("View Reports",
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