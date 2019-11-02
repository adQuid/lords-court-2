package ui

import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.image.ImageView
import javafx.scene.layout.*
import javafx.scene.text.Font
import javafx.scene.text.Text
import javafx.scene.text.TextAlignment

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
        val backgroundView = parent.generalComponents.makeImageView("assets//rooms//characterStage.png")
        val characterView = parent.generalComponents.makeImageView("assets//portraits//faceman.png")

        val nameText = Text(10.0, 50.0, parent.scene!!.conversation!!.otherParticipant(parent.playingAs()).player.fullName())
        nameText.font = Font(24.0)
        nameText.wrappingWidth = parent.totalWidth * 0.7
        nameText.textAlignment = TextAlignment.CENTER

        imagePane.children.addAll(backgroundView, characterView, nameText)
        StackPane.setAlignment(nameText, Pos.TOP_CENTER)

        return imagePane
    }

    private fun characterFocusButtons(): Pane {
        val cancelButton = parent.generalComponents.makeShortButton(
            "Cancel",
            EventHandler { parent.focusOn(parent.scene)})

        return parent.generalComponents.makeBottomPane(listOf(cancelButton))
    }
}