package ui.componentfactory

import game.Writ
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.image.ImageView
import javafx.scene.layout.GridPane
import javafx.scene.layout.Pane
import main.Controller
import shortstate.ShortGameScene
import shortstate.ShortStateCharacter
import shortstate.report.Report
import shortstate.room.Room
import shortstate.room.RoomAction
import ui.commoncomponents.selectionmodal.SelectionModal
import ui.commoncomponents.selectionmodal.Tab
import ui.totalHeight
import ui.totalWidth

class SceneComponentFactory {

    val scene: ShortGameScene
    var conversationComponentFactory: ConversationComponentFactory?

    constructor(scene: ShortGameScene){
        this.scene = scene
        if(scene.conversation != null){
            this.conversationComponentFactory = ConversationComponentFactory(scene.conversation!!)
        } else {
            this.conversationComponentFactory = null
        }
    }

    fun scenePage(perspective: ShortStateCharacter): Scene {
        val root = GridPane()

        if(scene.conversation != null){
            root.add(conversationComponentFactory!!.conversationPane(sceneImage(perspective), perspective), 0, 0)
            root.add(UtilityComponentFactory.bottomPane(conversationComponentFactory!!.buttons(perspective), perspective), 0, 1)
        } else {
            root.add(sceneImage(perspective), 0, 0)
            root.add(outOfConvoButtons(perspective), 0, 1)
        }

        val scene = Scene(root, totalWidth, totalHeight)
        return scene
    }

    fun sceneImage(perspective: ShortStateCharacter): Pane {
        val imagePane = Pane()
        val backgroundView: ImageView
        if(scene.characters!!.size > 1){
            val otherPlayer = scene.conversation!!.otherParticipant(perspective)
            backgroundView = UtilityComponentFactory.imageView(scene.room.pictureText)
            val characterView = UtilityComponentFactory.imageView(otherPlayer.player.pictureString)
            characterView.setOnMouseClicked { event -> Controller.singleton!!.GUI!!.focusOn(otherPlayer) }
            imagePane.children.addAll(backgroundView, characterView)
        } else {
            backgroundView = UtilityComponentFactory.imageView(scene.room.pictureText)
            imagePane.children.addAll(backgroundView)
        }
        return imagePane
    }

    private fun outOfConvoButtons(perspective: ShortStateCharacter): Pane {
        val btn1 = UtilityComponentFactory.shortButton("Personal Actions", null)
        val btn2 = if(scene.room.getActions(perspective.player).isNotEmpty()){
            UtilityComponentFactory.shortButton("Actions Here", EventHandler { _ ->
                Controller.singleton!!.GUI!!.focusOn(
                    SelectionModal(Controller.singleton!!.GUI!!,
                        roomActionButtons(scene.room, perspective),
                        { action ->
                            action.doAction(
                                Controller.singleton!!.shortThreadForPlayer(perspective),
                                perspective
                            ); Controller.singleton!!.GUI!!.display()
                        })
                )
            })
        } else {
            UtilityComponentFactory.shortButton("No Actions In this Room", null)
        }
        val btn3 = UtilityComponentFactory.newSceneButton(perspective)
        val btn4 = viewReportsButton(perspective)
        val btn5 = viewWritsButton(perspective)
        return UtilityComponentFactory.bottomPane(listOf(btn1,btn2,btn3,btn4,btn5), perspective)
    }

    private fun viewReportsButton(perspective: ShortStateCharacter): Button {
        return UtilityComponentFactory.shortButton("View Reports",
                EventHandler { _ -> Controller.singleton!!.GUI!!.focusOn(
                    SelectionModal(
                        Controller.singleton!!.GUI!!,
                        reports(perspective),
                        { report -> println(report) })
                )
            }
        )
    }

    private fun reports(perspective: ShortStateCharacter): List<Tab<Report>>{
        val reportOptions = perspective.knownReports.map{ report -> report }
        val reportTab = Tab<Report>("Reports", reportOptions)

        return listOf(reportTab)
    }

    private fun viewWritsButton(perspective: ShortStateCharacter): Button {
        return UtilityComponentFactory.shortButton("View Writs",
            EventHandler { _ -> Controller.singleton!!.GUI!!.focusOn(
                SelectionModal(
                    Controller.singleton!!.GUI!!,
                    writs(perspective),
                    { report -> println(report) })
            )
            }
        )
    }

    private fun writs(perspective: ShortStateCharacter): List<Tab<Writ>>{
        val writList = perspective.player.writs
        val retval = Tab<Writ>("Writs", writList)

        return listOf(retval)
    }

    private fun roomActionButtons(room: Room, perspective: ShortStateCharacter): List<Tab<RoomAction>>{
        val tab = Tab<RoomAction>(room.name, room.getActions(perspective.player))

        return listOf(tab)
    }
}