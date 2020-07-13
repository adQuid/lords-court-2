package ui.componentfactory

import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.layout.*
import javafx.scene.text.Font
import javafx.scene.text.Text
import javafx.scene.text.TextAlignment
import main.Controller
import main.UIGlobals
import shortstate.ShortStateCharacter
import ui.MAIN_WINDOW_PORTION
import ui.MainUI
import javax.naming.ldap.Control

class CharacterDetailComponentFactory {

    val character: ShortStateCharacter

    constructor(character: ShortStateCharacter){
        this.character = character
    }

    fun characterFocusPage(perspective: ShortStateCharacter): Scene {
        val root = GridPane()
        root.add(sceneImage(), 0, 0)
        root.add(MiddlePaneComponentFactory.middlePane(perspective, true), 0,1)
        val scene = Scene(root, UIGlobals.totalWidth(), UIGlobals.totalHeight())
        return scene
    }

    private fun sceneImage(): Pane {
        val imagePane = StackPane()
        val backgroundView = UtilityComponentFactory.imageView("assets/general/characterStage.png", MAIN_WINDOW_PORTION)
        val characterView = UtilityComponentFactory.imageView(character.player.pictureString, MAIN_WINDOW_PORTION)
        backgroundView.setOnMouseClicked { _ -> UIGlobals.defocus() }
        characterView.setOnMouseClicked { _ -> UIGlobals.defocus() }

        val nameText = Text(10.0, 50.0, character.player.fullName())
        nameText.font = Font(24.0)
        nameText.wrappingWidth = UIGlobals.totalWidth() * 0.7
        nameText.textAlignment = TextAlignment.CENTER

        imagePane.children.addAll(backgroundView, characterView, nameText)
        StackPane.setAlignment(nameText, Pos.TOP_CENTER)

        return imagePane
    }

    private fun characterFocusButtons(perspective: ShortStateCharacter): Pane {
        val cancelButton = UtilityComponentFactory.shortButton(
            "Cancel",
            EventHandler { UIGlobals.defocus()})

        return BottomPaneComponentFactory.bottomPane(listOf(cancelButton), perspective)
    }
}