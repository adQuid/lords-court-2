package ui.componentfactory

import shortstate.dialog.Line
import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.GridPane
import javafx.scene.layout.Pane
import javafx.scene.text.Font
import javafx.scene.text.Text
import main.Controller
import shortstate.dialog.linetypes.traits.HasAction
import shortstate.dialog.linetypes.traits.HasReportType
import ui.MainUI
import ui.MyAnchorPane
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
        val actionButtons =
        if(parent.shortGameScene!!.conversation!!.lastLine != null
            && parent.shortGameScene!!.conversation!!.lastLine!!.possibleReplies().isNotEmpty()){
            parent.shortGameScene!!.conversation!!.lastLine!!.possibleReplies()
                .map { line -> parent.utilityComponents.shortButton(line.tooltipName(), EventHandler {parent.focusOn(line); parent.display()}) }
        } else {
            parent.playingAs().defaultConversationLines().map{ line -> parent.utilityComponents.shortButton(line.tooltipName(), EventHandler { parent.focusOn(line); parent.display() })}
        }
        val leaveButton = parent.utilityComponents.newSceneButton()

        val pane = GridPane()
        pane.add(parent.conversationComponents.conversationBackgroundImage(), 0,0)
        pane.add(parent.utilityComponents.bottomPane(actionButtons + leaveButton), 0, 1)
        val scene = Scene(pane, parent.totalWidth, parent.totalHeight)
        return scene
    }

    fun lineConstructionOptions(): Scene {

        var buttonList = mutableListOf<Button>()
        if(parent.lineBeingConstructed is HasAction){
            buttonList.add(parent.utilityComponents.shortButton("Select Action",
                EventHandler { _ -> parent.focusOn(
                    SelectionModal(parent,
                        listOf(Tab("Basic Actions",Controller.singleton!!.game!!.possibleActionsForPlayer(parent.playingAs().player))),
                        { action ->
                            (parent.lineBeingConstructed as HasAction).mySetAction(action); parent.focusOn(parent.lineBeingConstructed)
                        })
                )})
            )
        }
        if(parent.lineBeingConstructed is HasReportType){
            buttonList.add(parent.utilityComponents.shortButton("Select Report",
                EventHandler { _ -> parent.focusOn(
                    SelectionModal(parent,
                        listOf(Tab("Reports",parent.playingAs().player.titles.flatMap { title -> title.reportsEntitled })),
                        { reportType ->
                            (parent.lineBeingConstructed as HasReportType).mySetReportType(reportType); parent.focusOn(parent.lineBeingConstructed)
                        })
                )})
            )
        }
        if(parent.lineBeingConstructed!!.validToSend()){
            buttonList.add(parent.utilityComponents.shortButton("Commit Line",
                EventHandler { _ -> parent.shortGameScene!!.conversation!!.submitLine(parent.lineBeingConstructed!!, parent.shortGame()); parent.lineBeingConstructed = null; parent.focusOn(parent.shortGameScene)}))
        }
        buttonList.add(parent.utilityComponents.shortButton(
            "Cancel",
            EventHandler { parent.focusOn(parent.shortGameScene)})
        )

        val pane = GridPane()
        pane.add(conversationBackgroundImage(), 0, 0)
        pane.add(parent.utilityComponents.bottomPane(buttonList), 0, 1)
        val scene = Scene(pane, parent.totalWidth, parent.totalHeight)
        return scene
    }

    fun conversationBackgroundImage(): Pane {
        val imagePane = parent.utilityComponents.sceneImage()

        if(parent.shortGameScene!!.conversation != null){
            val npcSpeechView = parent.utilityComponents.imageView("assets//general//leftSpeechBubble.png")
            val playerSpeechView = parent.utilityComponents.imageView("assets//general//rightSpeechBubble.png")
            imagePane.children.addAll(npcSpeechView, playerSpeechView)

            imagePane.children.add(descriptionPane())

            val lineAnchorPane = MyAnchorPane()
            linePane(lineAnchorPane, parent.lineBeingConstructed, myLineSymbolic, true)
            linePane(lineAnchorPane, parent.shortGameScene!!.conversation!!.lastLine, otherLineSymbolic, false)

            imagePane.children.add(lineAnchorPane.realPane)
        }
        return imagePane
    }

    private fun descriptionPane(): AnchorPane{
        val nameText = Text(10.0, 50.0, parent.shortGameScene!!.conversation!!.otherParticipant(parent.playingAs()).toString())
        nameText.font = Font(24.0)
        val descriptionAnchorPane = MyAnchorPane()
        descriptionAnchorPane.realPane.children.add(nameText)
        descriptionAnchorPane.setTopAnchor(nameText, parent.totalHeight * 0.03)
        descriptionAnchorPane.setLeftAnchor(nameText, parent.totalWidth * 0.35)
        return descriptionAnchorPane.realPane
    }

    private fun linePane(pane: MyAnchorPane, line: Line?, symbolic: Boolean, left: Boolean): MyAnchorPane {
        if(line == null){
            return pane
        }

        var lineNode: Node? = null

        if(symbolic){
            lineNode = GridPane()

            var index = 0 //gotta be a better way to do this
            line.symbolicForm().forEach { block ->
                val playerLineText = Text(block.toString())
                playerLineText.maxWidth(parent.totalWidth / 2)
                if (parent.totalWidth > 600.0) {
                    playerLineText.font = Font(20.0)
                }
                playerLineText.wrappingWidth = parent.totalWidth * 0.28
                (lineNode as GridPane).add(playerLineText, 0, index++)
            }
        } else {
            lineNode = Text(line.fullTextForm(parent.shortGameScene!!.conversation!!.lastSpeaker.player, parent.shortGameScene!!.conversation!!.otherParticipant(parent.shortGameScene!!.conversation!!.lastSpeaker).player))

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