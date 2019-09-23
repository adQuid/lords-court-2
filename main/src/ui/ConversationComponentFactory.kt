package ui

import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import javafx.scene.layout.Pane
import javafx.scene.text.Font
import javafx.scene.text.FontPosture
import javafx.scene.text.FontWeight
import javafx.scene.text.Text


class ConversationComponentFactory {

    val parent: MainUI

    var myLineSymbolic: Boolean = true
    var otherLineSymbolic: Boolean = false

    constructor(parent: MainUI){
        this.parent = parent
    }

    fun announceOptions(): Scene {
        val buttonsPane = GridPane()
        val btn1 = parent.generalComponents.makeShortButton("Action", EventHandler { _ -> println("yup that works") })
        buttonsPane.add(btn1, 0, 0)
        val btn2 = parent.generalComponents.makeShortButton( "filler", null)
        buttonsPane.add(btn2, 1, 0)
        val btn3 = parent.generalComponents.makeShortButton("filler", null)
        buttonsPane.add(btn3, 2, 0)
        val btn4 = parent.generalComponents.makeShortButton("filler", null)
        buttonsPane.add(btn4, 3, 0)
        val btn5 = parent.generalComponents.makeShortButton("filler", null)
        buttonsPane.add(btn5, 0, 1)
        val btn6 = parent.generalComponents.makeShortButton("filler", null)
        buttonsPane.add(btn6, 1, 1)
        val btn7 = parent.generalComponents.makeShortButton("filler", null)
        buttonsPane.add(btn7, 2, 1)
        val btn8 = parent.generalComponents.makeShortButton(
            "Cancel",
            EventHandler { parent.setFocus(parent.conversation); parent.display() })
        buttonsPane.add(btn8, 3, 1)

        val pane = GridPane()
        pane.add(conversationBackgroundImage(), 0, 0)
        pane.add(buttonsPane, 0, 1)
        val scene = Scene(pane, parent.totalWidth, parent.totalHeight)
        return scene
    }

    fun conversationBackgroundImage(): Pane {
        val imagePane = parent.generalComponents.mainImage()
        val npcSpeechView = parent.generalComponents.makeImageView("assets//general//NPCSpeechBubble.png")

        val playerSpeechView = parent.generalComponents.makeImageView("assets//general//PlayerSpeechBubble.png")
        imagePane.children.addAll(npcSpeechView, playerSpeechView)

        if(parent.line != null) {
            if(myLineSymbolic){
                var linePane = GridPane()
                var index = 0 //gotta be a better way to do this
                parent.line!!.symbolicForm().forEach { block ->
                    val playerLineText = Text(block.text)
                    playerLineText.maxWidth(parent.totalWidth / 2)
                    if (parent.totalWidth > 600.0) {
                        playerLineText.font = Font(20.0)
                    }
                    playerLineText.wrappingWidth = parent.totalWidth / 4
                    linePane.add(playerLineText, 0, index++)
                }
                linePane.setOnMouseClicked { _ -> myLineSymbolic = !myLineSymbolic; parent.display() }
                val anchor = MyAnchorPane(linePane)

                anchor.setTopAnchor(linePane, parent.totalHeight * 0.03);
                anchor.setLeftAnchor(linePane, parent.totalWidth * 0.03);
                imagePane.children.add(anchor.realPane)
            } else {
                val playerLineText = Text(parent.line!!.fullTextForm())
                playerLineText.maxWidth(parent.totalWidth / 2)
                if (parent.totalWidth > 800.0) {
                    playerLineText.font = Font(16.0)
                }
                playerLineText.wrappingWidth = parent.totalWidth / 4
                playerLineText.setOnMouseClicked { _ -> myLineSymbolic = !myLineSymbolic; parent.display() }
                val anchor = MyAnchorPane(playerLineText)

                anchor.setTopAnchor(playerLineText, parent.totalHeight * 0.03);
                anchor.setLeftAnchor(playerLineText, parent.totalWidth * 0.03);
                imagePane.children.add(anchor.realPane)
            }
        }
        return imagePane
    }
}