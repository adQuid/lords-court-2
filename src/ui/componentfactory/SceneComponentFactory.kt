package ui.componentfactory

import javafx.scene.Scene
import javafx.scene.image.ImageView
import javafx.scene.layout.GridPane
import javafx.scene.layout.Pane
import main.UIGlobals
import shortstate.ShortGameScene
import shortstate.ShortStateCharacter

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
        root.add(MiddlePaneComponentFactory.middlePane(perspective), 0, 1)
        root.add(BottomPaneComponentFactory.sceneBottomPane(scene, perspective), 0, 2)

        return Scene(root, UIGlobals.totalWidth(), UIGlobals.totalHeight())
    }

    fun sceneImage(perspective: ShortStateCharacter): Pane {
        val imagePane = Pane()
        val backgroundView = UtilityComponentFactory.imageView(scene.room.pictureText+"/inside", 0.8)
        if(scene.characters!!.size > 1){
            val otherPlayer = scene.conversation!!.otherParticipant(perspective)
            val characterView = UtilityComponentFactory.imageView(otherPlayer.player.pictureString, 0.8)
            characterView.setOnMouseClicked { event -> UIGlobals.focusOn(otherPlayer) }
            imagePane.children.addAll(backgroundView, characterView)
        } else {
            imagePane.children.addAll(backgroundView)
        }
        return imagePane
    }
}