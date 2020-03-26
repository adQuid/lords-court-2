package ui.componentfactory

import shortstate.dialog.Line
import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.GridPane
import javafx.scene.layout.Pane
import javafx.scene.text.Font
import javafx.scene.text.Text
import main.Controller
import main.UIGlobals
import shortstate.Conversation
import shortstate.ShortStateCharacter
import ui.MyAnchorPane
import ui.totalHeight
import ui.totalWidth


class ConversationComponentFactory {

    val conversation: Conversation
    var lineBeingConstructed: Line? = null

    var myLineSymbolic: Boolean = true
    var otherLineSymbolic: Boolean = false

    constructor(conversation: Conversation){
        this.conversation = conversation
    }

    fun buttons(perspective: ShortStateCharacter): List<Button> {
        var retval = mutableListOf<Button>()
        if(lineBeingConstructed == null){
            val linesList =
            if(conversation.lastLine != null
                && conversation.lastLine!!.possibleReplies().isNotEmpty()){
                conversation.lastLine!!.possibleReplies()
            } else {
                conversation.defaultConversationLines(perspective)
            }
            retval = linesList
                .map { line -> UtilityComponentFactory.shortButton(line.tooltipName(), EventHandler {focusOnLine(line); UIGlobals.refresh()})}.toMutableList()
            retval.add(UtilityComponentFactory.newSceneButton(perspective))
        }

        return retval
    }

    fun conversationPane(backgroundPane: Pane, perspective: ShortStateCharacter): Pane {

        if(conversation != null){
            val npcSpeechView = UtilityComponentFactory.imageView("assets/general/leftSpeechBubble.png")
            npcSpeechView.setOnMouseClicked { _ -> myLineSymbolic = !myLineSymbolic; UIGlobals.refresh() }
            val playerSpeechView = UtilityComponentFactory.imageView("assets/general/rightSpeechBubble.png")
            playerSpeechView.setOnMouseClicked { _ -> otherLineSymbolic = !otherLineSymbolic; UIGlobals.refresh() }
            backgroundPane.children.addAll(npcSpeechView, playerSpeechView)

            if(lineBeingConstructed != null){
                if(lineBeingConstructed!!.validToSend()){
                    val sendButton = UtilityComponentFactory.imageView("assets/general/talkButton.png")
                    sendButton.setOnMouseClicked { _ -> conversation.submitLine(lineBeingConstructed!!, Controller.singleton!!.shortThreadForPlayer(perspective).shortGame);
                        lineBeingConstructed = null; UIGlobals.defocus()}
                    backgroundPane.children.add(sendButton)
                }
                val cancelButton = UtilityComponentFactory.imageView("assets/general/cancelLineButton.png")
                cancelButton.setOnMouseClicked { lineBeingConstructed = null; UIGlobals.refresh()}
                backgroundPane.children.add(cancelButton)
            }

            backgroundPane.children.add(descriptionPane(perspective))

            val lineAnchorPane = MyAnchorPane()
            linePane(perspective, lineAnchorPane, lineBeingConstructed, myLineSymbolic, true)
            linePane(perspective, lineAnchorPane, conversation.lastLine, otherLineSymbolic, false)

            backgroundPane.children.add(lineAnchorPane.realPane)
        }
        return backgroundPane
    }

    private fun descriptionPane(perspective: ShortStateCharacter): AnchorPane{
        val nameText = Text(10.0, 50.0, conversation.otherParticipant(perspective).toString())
        nameText.font = Font(24.0)
        val descriptionAnchorPane = MyAnchorPane()
        descriptionAnchorPane.realPane.children.add(nameText)
        descriptionAnchorPane.setTopAnchor(nameText, totalHeight * 0.03)
        descriptionAnchorPane.setLeftAnchor(nameText, totalWidth * 0.35)
        return descriptionAnchorPane.realPane
    }

    private fun linePane(perspective: ShortStateCharacter, pane: MyAnchorPane, line: Line?, symbolic: Boolean, left: Boolean): MyAnchorPane {
        if(line == null){
            return pane
        }

        var lineNode: Node? = null

        if(symbolic){
            lineNode = GridPane()

            var index = 0 //gotta be a better way to do this
            line.symbolicForm(conversation.lastSpeaker, conversation.otherParticipant(conversation.lastSpeaker)).forEach { block ->
                val playerLineText = block.textForm(perspective)
                playerLineText.maxWidth(totalWidth / 2)
                if (totalWidth > 600.0) {
                    playerLineText.font = Font(20.0)
                }
                playerLineText.wrappingWidth = totalWidth * 0.28
                (lineNode as GridPane).add(playerLineText, 0, index++)

                if(block.behavior==null){
                    if(left){
                        playerLineText.setOnMouseClicked { _ -> myLineSymbolic = !myLineSymbolic; UIGlobals.refresh() }
                    } else {
                        playerLineText.setOnMouseClicked { _ -> otherLineSymbolic = !otherLineSymbolic; UIGlobals.refresh() }
                    }
                }
            }
        } else {
            lineNode = Text(line.fullTextForm(conversation.lastSpeaker, conversation.otherParticipant(conversation.lastSpeaker)))

            lineNode.maxWidth(totalWidth / 2)
            if (totalWidth > 800.0) {
                lineNode.font = Font(16.0)
            }
            lineNode.wrappingWidth = totalWidth * 0.28

            if(left){
                lineNode.setOnMouseClicked { _ -> myLineSymbolic = !myLineSymbolic; UIGlobals.refresh() }
            } else {
                lineNode.setOnMouseClicked { _ -> otherLineSymbolic = !otherLineSymbolic; UIGlobals.refresh() }
            }
        }

        pane.realPane.children.add(lineNode)

        pane.setTopAnchor(lineNode, totalHeight * 0.03);
        if(left){
            pane.setLeftAnchor(lineNode, totalWidth * 0.03);
        } else {
            pane.setLeftAnchor(lineNode, totalWidth * 0.693);
        }

        return pane
    }

    fun focusOnLine(line: Line){
        lineBeingConstructed = line
    }

}