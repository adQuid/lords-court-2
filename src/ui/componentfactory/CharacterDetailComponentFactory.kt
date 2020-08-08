package ui.componentfactory

import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.layout.*
import javafx.scene.text.Font
import javafx.scene.text.Text
import javafx.scene.text.TextAlignment
import main.UIGlobals
import shortstate.ShortStateCharacter
import ui.MAIN_WINDOW_PORTION

class CharacterDetailComponentFactory {

    companion object{
        fun characterNameText(character: ShortStateCharacter, full:Boolean): Node {
            val nameString = if(full){
                character.player.fullName()
            }else{
                character.player.toString()
            }
            val nameText = Text(10.0, 50.0, nameString)
            nameText.font = Font(24.0)
            nameText.textAlignment = TextAlignment.CENTER
            nameText.x = UIGlobals.totalWidth() * 0.5 - (nameText.boundsInLocal.width * 0.5)
            nameText.y = UIGlobals.totalHeight() * 0.05
            if(full){
                if(nameString.length > 30){
                    nameText.x = UIGlobals.totalWidth() * 0.55 - (nameText.boundsInLocal.width * 0.5)
                }
                nameText.textAlignment = TextAlignment.JUSTIFY
                nameText.wrappingWidth = UIGlobals.totalWidth() * 0.65
            }
            return nameText
        }
    }

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
        val imagePane = Pane()
        val backgroundView = UtilityComponentFactory.imageView("assets/general/characterStage.png", MAIN_WINDOW_PORTION)
        val characterView = UtilityComponentFactory.imageView(character.player.pictureString, MAIN_WINDOW_PORTION)
        backgroundView.setOnMouseClicked { _ -> UIGlobals.defocus() }
        characterView.setOnMouseClicked { _ -> UIGlobals.defocus() }

        val nameText = characterNameText(character, true)

        imagePane.children.addAll(backgroundView, characterView, nameText)

        return imagePane
    }
}