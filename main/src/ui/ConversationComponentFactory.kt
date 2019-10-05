package ui

import dialog.Line
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

    fun inConvoPage(): Scene{
        val buttonsPane = GridPane()
        val btn1 = parent.generalComponents.makeShortButton("Announce", EventHandler { parent.focusOn(Announcement(null)); parent.display() })
        buttonsPane.add(btn1, 0,0)
        val btn2 = parent.generalComponents.makeShortButton("filler", null)
        buttonsPane.add(btn2, 1,0)
        val btn3 = parent.generalComponents.makeShortButton("filler", null)
        buttonsPane.add(btn3, 2,0)
        val btn4 = parent.generalComponents.makeShortButton("filler", null)
        buttonsPane.add(btn4, 3,0)
        val btn5 = parent.generalComponents.makeShortButton("filler", null)
        buttonsPane.add(btn5, 0,1)
        val btn6 = parent.generalComponents.makeShortButton("filler", null)
        buttonsPane.add(btn6, 1,1)
        val btn7 = parent.generalComponents.makeShortButton("filler", null)
        buttonsPane.add(btn7, 2,1)
        val btn8 = parent.generalComponents.makeShortButton("Leave", EventHandler{ _ -> /*Controller.singleton!!.endConversation(parent.conversation!!);*/ parent.focusOn(null)})
        buttonsPane.add(btn8, 3,1)

        val pane = GridPane()
        pane.add(parent.conversationComponents.conversationBackgroundImage(), 0,0)
        pane.add(buttonsPane, 0, 1)
        val scene = Scene(pane, parent.totalWidth, parent.totalHeight)
        return scene
    }

    fun announceOptions(): Scene {
        val buttonsPane = GridPane()
        val btn1 = parent.generalComponents.makeShortButton("Select Action",
            EventHandler { _ -> parent.focusOn(ActionSelectModal(parent, { action -> parent.lineBeingConstructed = Announcement(action); parent.focusOn(parent.lineBeingConstructed)})) })
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
        val btn7 = parent.generalComponents.makeShortButton("Declare Announcement",
            EventHandler { _ -> parent.scene!!.conversation!!.submitLine(parent.lineBeingConstructed!!, Controller.singleton!!.game!!); parent.focusOn(parent.scene!!.conversation) })
        buttonsPane.add(btn7, 2, 1)
        val btn8 = parent.generalComponents.makeShortButton(
            "Cancel",
            EventHandler { parent.focusOn(parent.scene)})
        buttonsPane.add(btn8, 3, 1)

        val pane = GridPane()
        pane.add(conversationBackgroundImage(), 0, 0)
        pane.add(buttonsPane, 0, 1)
        val scene = Scene(pane, parent.totalWidth, parent.totalHeight)
        return scene
    }

    fun conversationBackgroundImage(): Pane {
        val imagePane = parent.generalComponents.sceneImage()

        if(parent.scene!!.conversation != null){
            val npcSpeechView = parent.generalComponents.makeImageView("assets//general//leftSpeechBubble.png")
            val playerSpeechView = parent.generalComponents.makeImageView("assets//general//rightSpeechBubble.png")
            imagePane.children.addAll(npcSpeechView, playerSpeechView)

            val lineAnchorPane = MyAnchorPane()
            linePane(lineAnchorPane, parent.lineBeingConstructed, myLineSymbolic, true)
            linePane(lineAnchorPane, parent.scene!!.conversation!!.lastLine, otherLineSymbolic, false)

            imagePane.children.add(lineAnchorPane.realPane)
        }
        return imagePane
    }

    private fun linePane(pane: MyAnchorPane, line: Line?, symbolic: Boolean, left: Boolean): MyAnchorPane{
        if(line == null){
            return pane
        }

        var lineNode: Node? = null

        if(symbolic){
            lineNode = GridPane()

            var index = 0 //gotta be a better way to do this
            line.symbolicForm().forEach { block ->
                val playerLineText = Text(block.text)
                playerLineText.maxWidth(parent.totalWidth / 2)
                if (parent.totalWidth > 600.0) {
                    playerLineText.font = Font(20.0)
                }
                playerLineText.wrappingWidth = parent.totalWidth * 0.28
                (lineNode as GridPane).add(playerLineText, 0, index++)
            }
        } else {
            lineNode = Text(line.fullTextForm())

            lineNode.maxWidth(parent.totalWidth / 2)
            if (parent.totalWidth > 800.0) {
                lineNode.font = Font(16.0)
            }
            lineNode.wrappingWidth = parent.totalWidth * 0.28
        }

        if(left){
            lineNode.setOnMouseClicked { _ -> myLineSymbolic = !myLineSymbolic; parent.display() }
        } else {
            lineNode.setOnMouseClicked { _ -> otherLineSymbolic = !otherLineSymbolic; parent.display() }
        }

        pane.realPane.children.add(lineNode)

        pane.setTopAnchor(lineNode, parent.totalHeight * 0.03);
        if(left){
            pane.setLeftAnchor(lineNode, parent.totalWidth * 0.03);
        } else {
            pane.setRightAnchor(lineNode, parent.totalWidth * 0.027);
        }

        return pane
    }
}