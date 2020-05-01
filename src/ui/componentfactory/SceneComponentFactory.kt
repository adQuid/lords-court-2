package ui.componentfactory

import game.Writ
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.image.ImageView
import javafx.scene.layout.GridPane
import javafx.scene.layout.Pane
import main.Controller
import main.UIGlobals
import shortstate.ShortGameScene
import shortstate.ShortStateCharacter
import shortstate.report.Report
import shortstate.room.Room
import shortstate.room.RoomAction
import shortstate.room.RoomActionMaker
import ui.specialdisplayables.selectionmodal.SelectionModal
import ui.specialdisplayables.selectionmodal.Tab

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

        var bottomButtonList = universalButtons(perspective)
        if(scene.conversation != null){
            root.add(conversationComponentFactory!!.conversationPane(sceneImage(perspective), perspective), 0, 0)
        } else {
            bottomButtonList = bottomButtonList.plus(outOfConvoButtons(perspective))
            root.add(sceneImage(perspective), 0, 0)
        }
        root.add(UtilityComponentFactory.bottomPane(bottomButtonList, perspective), 0, 1)

        val scene = Scene(root, UIGlobals.totalWidth(), UIGlobals.totalHeight())
        return scene
    }

    fun sceneImage(perspective: ShortStateCharacter): Pane {
        val imagePane = Pane()
        val backgroundView: ImageView
        if(scene.characters!!.size > 1){
            val otherPlayer = scene.conversation!!.otherParticipant(perspective)
            backgroundView = UtilityComponentFactory.imageView(scene.room.pictureText)
            val characterView = UtilityComponentFactory.imageView(otherPlayer.player.pictureString)
            characterView.setOnMouseClicked { event -> UIGlobals.focusOn(otherPlayer) }
            imagePane.children.addAll(backgroundView, characterView)
        } else {
            backgroundView = UtilityComponentFactory.imageView(scene.room.pictureText)
            imagePane.children.addAll(backgroundView)
        }
        return imagePane
    }

    private fun universalButtons(perspective: ShortStateCharacter): List<Button> {
        return listOf(UtilityComponentFactory.newSceneButton(perspective), viewReportsButton(perspective), viewWritsButton(perspective))
    }

    private fun outOfConvoButtons(perspective: ShortStateCharacter): List<Button> {
        val btn2 = if(scene.room.getActions(perspective).isNotEmpty()){
            UtilityComponentFactory.shortButton("Actions Here", EventHandler { _ ->
                UIGlobals.focusOn(
                    SelectionModal(
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

        return listOf(btn2)
    }

    private fun viewReportsButton(perspective: ShortStateCharacter): Button {
        return UtilityComponentFactory.shortButton("View Reports",
                EventHandler { _ -> UIGlobals.focusOn(
                    SelectionModal(
                        reports(perspective),
                        { report -> println(report) })
                )
            }
        )
    }

    private fun reports(perspective: ShortStateCharacter): List<Tab<Report>>{
        val reportOptions = perspective.knownReports
        val reportTab = Tab<Report>("Reports", reportOptions)

        return listOf(reportTab)
    }

    private fun viewWritsButton(perspective: ShortStateCharacter): Button {
        return UtilityComponentFactory.shortButton("View Writs",
            EventHandler { _ -> UIGlobals.focusOn(
                SelectionModal(
                    writs(perspective),
                    {})
            )
            }
        )
    }

    private fun writs(perspective: ShortStateCharacter): List<Tab<Writ>>{
        val complete = perspective.player.writs.filter { it.complete() }
        val completeTab = Tab<Writ>("Complete", complete)
        val incomplete = perspective.player.writs.filter { !it.complete() }
        val incompleteTab = Tab<Writ>("Incomplete", incomplete)

        return listOf(completeTab, incompleteTab)
    }

    private fun roomActionButtons(room: Room, perspective: ShortStateCharacter): List<Tab<RoomActionMaker>>{
        val tab = Tab(room.name, room.getActions(perspective))

        return listOf(tab)
    }
}