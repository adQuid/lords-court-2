package ui.componentfactory

import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import javafx.scene.layout.Pane
import main.Controller
import main.UIGlobals
import shortstate.ShortGameScene
import shortstate.ShortStateCharacter
import shortstate.room.Room
import shortstate.room.RoomActionMaker
import ui.MAIN_WINDOW_PORTION
import ui.MainUI
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

        if(scene.conversation != null){
            root.add(conversationComponentFactory!!.conversationPane(sceneImage(perspective), perspective), 0, 0)
        } else {
            root.add(sceneImage(perspective), 0, 0)
        }
        root.add(MiddlePaneComponentFactory.middlePane(perspective, scene.conversation != null), 0, 1)
        //root.add(BottomPaneComponentFactory.sceneBottomPane(scene, perspective), 0, 2)

        return Scene(root, UIGlobals.totalWidth(), UIGlobals.totalHeight())
    }

    fun sceneImage(perspective: ShortStateCharacter): Pane {
        val imagePane = Pane()
        val backgroundView = UtilityComponentFactory.imageView(scene.room.imagePath+"/inside", MAIN_WINDOW_PORTION)

        val interactView = UtilityComponentFactory.imageView(scene.room.imagePath+"/interact", MAIN_WINDOW_PORTION)
        UtilityComponentFactory.applyTooltip(interactView, Room.tooltips[scene.room.type])
        interactView.onMouseClicked = EventHandler { _ ->
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
        }

        interactView.onMouseEntered = EventHandler { _ -> interactView.image = UtilityComponentFactory.imageFromPath(scene.room.imagePath+"/interact_highlight"); }
        interactView.onMouseExited = EventHandler { _ -> interactView.image = UtilityComponentFactory.imageFromPath(scene.room.imagePath+"/interact"); }

        imagePane.children.addAll(backgroundView, interactView)
        if(scene.characters!!.size > 1){
            val otherPlayer = scene.conversation!!.otherParticipant(perspective)
            val characterView = UtilityComponentFactory.imageView(otherPlayer.player.pictureString, MAIN_WINDOW_PORTION)
            characterView.setOnMouseClicked { event -> UIGlobals.focusOn(otherPlayer) }
            imagePane.children.addAll(characterView)
        } else {
        }
        return imagePane
    }

    private fun roomActionButtons(room: Room, perspective: ShortStateCharacter): List<Tab<RoomActionMaker>>{
        val tab = Tab(room.name, room.getActions(perspective))

        return listOf(tab)
    }
}