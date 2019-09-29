package ui

import dialog.linetypes.Announcement
import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import javafx.scene.layout.Pane
import javafx.scene.text.Font
import javafx.scene.text.Text
import main.Controller


class ConversationComponentFactory {

    val parent: MainUI

    var myLineSymbolic: Boolean = true
    var otherLineSymbolic: Boolean = false

    constructor(parent: MainUI){
        this.parent = parent
    }

    fun announceOptions(): Scene {
        val buttonsPane = GridPane()
        val btn1 = parent.generalComponents.makeShortButton("Select Action", EventHandler { _ -> parent.focusOn(ActionSelectModal(parent, { action -> parent.line = Announcement(action); parent.focusOn(parent.line)})) })
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
        val btn7 = parent.generalComponents.makeShortButton("Declare Announcement", EventHandler { _ -> parent.conversation!!.submitLine(parent.line!!); parent.display() })
        buttonsPane.add(btn7, 2, 1)
        val btn8 = parent.generalComponents.makeShortButton(
            "Cancel",
            EventHandler { parent.focusOn(parent.conversation)})
        buttonsPane.add(btn8, 3, 1)

        val pane = GridPane()
        pane.add(conversationBackgroundImage(), 0, 0)
        pane.add(buttonsPane, 0, 1)
        val scene = Scene(pane, parent.totalWidth, parent.totalHeight)
        return scene
    }

    fun conversationBackgroundImage(): Pane {
        val imagePane = parent.generalComponents.mainImage()

        val npcSpeechView = parent.generalComponents.makeImageView("assets//general//leftSpeechBubble.png")
        val playerSpeechView = parent.generalComponents.makeImageView("assets//general//rightSpeechBubble.png")
        imagePane.children.addAll(npcSpeechView, playerSpeechView)

        if(parent.line != null){
            var lineNode: Node? = null

            if(myLineSymbolic){
                lineNode = GridPane()

                var index = 0 //gotta be a better way to do this
                parent.line!!.symbolicForm().forEach { block ->
                    val playerLineText = Text(block.text)
                    playerLineText.maxWidth(parent.totalWidth / 2)
                    if (parent.totalWidth > 600.0) {
                        playerLineText.font = Font(20.0)
                    }
                    playerLineText.wrappingWidth = parent.totalWidth * 0.28
                    (lineNode as GridPane).add(playerLineText, 0, index++)
                }
                lineNode.setOnMouseClicked { _ -> myLineSymbolic = !myLineSymbolic; parent.display() }
            } else {
                lineNode = Text(parent.line!!.fullTextForm())

                lineNode.maxWidth(parent.totalWidth / 2)
                if (parent.totalWidth > 800.0) {
                    lineNode.font = Font(16.0)
                }
                lineNode.wrappingWidth = parent.totalWidth * 0.28
                lineNode.setOnMouseClicked { _ -> myLineSymbolic = !myLineSymbolic; parent.display() }
            }

            val anchor = MyAnchorPane(lineNode)
            anchor.setTopAnchor(lineNode, parent.totalHeight * 0.03);
            if(parent.conversation!!.lastSpeaker != Controller.singleton!!.game!!.playerCharacter()){
                anchor.setLeftAnchor(lineNode, parent.totalWidth * 0.03);
            } else {
                anchor.setRightAnchor(lineNode, parent.totalWidth * 0.027);
            }

            imagePane.children.add(anchor.realPane)
        }

        return imagePane
    }
}