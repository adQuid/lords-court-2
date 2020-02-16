package ui.componentfactory

import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.layout.*
import javafx.scene.text.Font
import javafx.scene.text.Text
import javafx.scene.text.TextAlignment
import main.Controller
import shortstate.ShortStateCharacter
import ui.MainUI
import ui.totalHeight
import ui.totalWidth
import javax.naming.ldap.Control

class CharacterDetailComponentFactory {

    val character: ShortStateCharacter

    constructor(character: ShortStateCharacter){
        this.character = character
    }

    fun characterFocusPage(perspective: ShortStateCharacter): Scene {
        val root = GridPane()
        root.add(characterFocusButtons(perspective), 0, 1)
        root.add(sceneImage(), 0, 0)
        val scene = Scene(root, totalWidth, totalHeight)
        return scene
    }

    private fun sceneImage(): Pane {
        val imagePane = StackPane()
        val backgroundView = UtilityComponentFactory.imageView("assets//rooms//characterStage.png")
        val characterView = UtilityComponentFactory.imageView(character.player.pictureString)
        backgroundView.setOnMouseClicked { event -> Controller.singleton!!.GUI!!.defocus() }
        characterView.setOnMouseClicked { event -> Controller.singleton!!.GUI!!.defocus() }

        val nameText = Text(10.0, 50.0, character.player.fullName())
        nameText.font = Font(24.0)
        nameText.wrappingWidth = totalWidth * 0.7
        nameText.textAlignment = TextAlignment.CENTER

        imagePane.children.addAll(backgroundView, characterView, nameText)
        StackPane.setAlignment(nameText, Pos.TOP_CENTER)

        return imagePane
    }

    private fun characterFocusButtons(perspective: ShortStateCharacter): Pane {
        val cancelButton = UtilityComponentFactory.shortButton(
            "Cancel",
            EventHandler { Controller.singleton!!.GUI!!.defocus()})

        return UtilityComponentFactory.bottomPane(listOf(cancelButton), perspective)
    }
}