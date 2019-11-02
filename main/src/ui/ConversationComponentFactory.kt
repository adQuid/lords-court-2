package ui

import shortstate.dialog.Line
import shortstate.dialog.linetypes.Announcement
import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.GridPane
import javafx.scene.layout.Pane
import javafx.scene.layout.StackPane
import javafx.scene.text.Font
import javafx.scene.text.Text
import main.Controller
import ui.selectionmodal.SelectionModal
import ui.selectionmodal.Tab


class ConversationComponentFactory {

    val parent: MainUI

    var myLineSymbolic: Boolean = true
    var otherLineSymbolic: Boolean = false

    constructor(parent: MainUI){
        this.parent = parent
    }

    fun inConvoPage(): Scene{
        val btn1 = parent.generalComponents.makeShortButton("Announce", EventHandler { parent.focusOn(Announcement(null)); parent.display() })
        val leaveButton = parent.sceneComponents.newSceneButton()

        val pane = GridPane()
        pane.add(parent.conversationComponents.conversationBackgroundImage(), 0,0)
        pane.add(parent.generalComponents.makeBottomPane(listOf(btn1,leaveButton)), 0, 1)
        val scene = Scene(pane, parent.totalWidth, parent.totalHeight)
        return scene
    }

    fun announceOptions(): Scene {
        val btn1 = parent.generalComponents.makeShortButton("Select Action",
            EventHandler { _ -> parent.focusOn(
                SelectionModal(parent,
                    listOf(Tab("Basic Actions",Controller.singleton!!.game!!.possibleActionsForPlayer(parent.playingAs().player))),
                    { action ->
                        parent.lineBeingConstructed = Announcement(action); parent.focusOn(parent.lineBeingConstructed)
                    })
            ) })
        val btn7 = parent.generalComponents.makeShortButton("Declare Announcement",
            EventHandler { _ -> parent.scene!!.conversation!!.submitLine(parent.lineBeingConstructed!!, parent.shortGame()); parent.lineBeingConstructed = null; parent.focusOn(parent.scene)})
        val btn8 = parent.generalComponents.makeShortButton(
            "Cancel",
            EventHandler { parent.focusOn(parent.scene)})

        val pane = GridPane()
        pane.add(conversationBackgroundImage(), 0, 0)
        pane.add(parent.generalComponents.makeBottomPane(listOf(btn1,btn7,btn8)), 0, 1)
        val scene = Scene(pane, parent.totalWidth, parent.totalHeight)
        return scene
    }

    fun conversationBackgroundImage(): Pane {
        val imagePane = parent.generalComponents.sceneImage()

        if(parent.scene!!.conversation != null){
            val npcSpeechView = parent.generalComponents.makeImageView("assets//general//leftSpeechBubble.png")
            val playerSpeechView = parent.generalComponents.makeImageView("assets//general//rightSpeechBubble.png")
            imagePane.children.addAll(npcSpeechView, playerSpeechView)

            imagePane.children.add(descriptionPane())

            val lineAnchorPane = MyAnchorPane()
            linePane(lineAnchorPane, parent.lineBeingConstructed, myLineSymbolic, true)
            linePane(lineAnchorPane, parent.scene!!.conversation!!.lastLine, otherLineSymbolic, false)

            imagePane.children.add(lineAnchorPane.realPane)
        }
        return imagePane
    }

    private fun descriptionPane(): AnchorPane{
        val nameText = Text(10.0, 50.0, parent.scene!!.conversation!!.otherParticipant(parent.playingAs()).toString())
        nameText.font = Font(24.0)
        val descriptionAnchorPane = MyAnchorPane()
        descriptionAnchorPane.realPane.children.add(nameText)
        descriptionAnchorPane.setTopAnchor(nameText, parent.totalHeight * 0.03)
        descriptionAnchorPane.setLeftAnchor(nameText, parent.totalWidth * 0.35)
        return descriptionAnchorPane.realPane
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
            pane.setLeftAnchor(lineNode, parent.totalWidth * 0.693);
        }

        return pane
    }
}