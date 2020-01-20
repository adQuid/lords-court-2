package ui.componentfactory

import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.layout.*
import javafx.scene.text.Font
import javafx.scene.text.Text
import javafx.scene.text.TextAlignment
import ui.MainUI

class CharacterDetailComponentFactory {

    val parent: MainUI

    constructor(parent: MainUI){
        this.parent = parent
    }

    fun characterFocusPage(): Scene {
        val root = GridPane()
        root.add(characterFocusButtons(), 0, 1)
        root.add(sceneImage(), 0, 0)
        val scene = Scene(root, parent.totalWidth, parent.totalHeight)
        return scene
    }

    private fun sceneImage(): Pane {
        val imagePane = StackPane()
        val backgroundView = parent.generalComponents.imageView("assets//rooms//characterStage.png")
        val characterView = parent.generalComponents.imageView(parent.character!!.pictureString)
        backgroundView.setOnMouseClicked { event -> parent.defocus() }
        characterView.setOnMouseClicked { event -> parent.defocus() }

        val nameText = Text(10.0, 50.0, parent.scene!!.conversation!!.otherParticipant(parent.playingAs()).player.fullName())
        nameText.font = Font(24.0)
        nameText.wrappingWidth = parent.totalWidth * 0.7
        nameText.textAlignment = TextAlignment.CENTER

        imagePane.children.addAll(backgroundView, characterView, nameText)
        StackPane.setAlignment(nameText, Pos.TOP_CENTER)

        return imagePane
    }

    private fun characterFocusButtons(): Pane {
        val cancelButton = parent.generalComponents.shortButton(
            "Cancel",
            EventHandler { parent.focusOn(parent.scene)})

        return parent.generalComponents.bottomPane(listOf(cancelButton))
    }
}