package ui

import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.layout.GridPane
import javafx.scene.layout.Pane
import shortstate.report.Report
import shortstate.room.Room
import shortstate.room.RoomAction
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
        val btn2 = if(parent.scene!!.room.baseActions.isNotEmpty() || parent.scene!!.room.type == Room.RoomType.WORKROOM){
            parent.generalComponents.makeShortButton("Actions Here", EventHandler { _ ->
                parent.focusOn(SelectionModal(parent, roomActionButtons(parent.scene!!.room), {action -> action.doAction(parent.shortGame(), parent.playingAs()); parent.refocus()}))
            })
        } else {
            parent.generalComponents.makeShortButton("No Actions In this Room", null)
        }
        val btn3 = newSceneButton()
        val btn4 = viewReportsButton()
        return parent.generalComponents.makeBottomPane(listOf(btn1,btn2,btn3,btn4))
    }

    fun viewReportsButton(): Button {
        return parent.generalComponents.makeShortButton("View Reports",
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

    fun newSceneButton(): Button {
        return parent.generalComponents.makeShortButton("Go Somewhere Else",
            EventHandler { _ -> parent.focusOn(
                SelectionModal(parent, newSceneOptions(), { maker -> goToNewSceneIfApplicable(maker)})
            )
            }
        )
    }

    private fun newSceneOptions(): List<Tab<SceneMaker>>{
        val goToRoomMakers = parent.playingAs().player.location.rooms.map { room -> GoToRoomSoloMaker(parent.playingAs(), room) }
        val goToRoomTab = Tab<SceneMaker>("Go to Room", goToRoomMakers)

        val conversationMakers = parent.shortGame().players.minusElement(parent.playingAs())
            .map { player -> ConversationMaker(parent.playingAs(), player, parent.playingAs().player.location.roomByType(Room.RoomType.ETC)) }
        val conversationTab = Tab<SceneMaker>("Conversation", conversationMakers)

        return listOf(goToRoomTab, conversationTab)
    }

    private fun goToNewSceneIfApplicable(maker: SceneMaker){
        parent.playingAs().nextSceneIWannaBeIn = maker
        parent.shortGame().endScene(parent.scene!!)
        parent.refocus()
    }

    private fun roomActionButtons(room: Room): List<Tab<RoomAction>>{
        val tab = Tab<RoomAction>(room.name, room.getActions(parent.playingAs().player))

        return listOf(tab)
    }
}