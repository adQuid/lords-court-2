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
import shortstate.Conversation
import shortstate.ShortStateCharacter
import shortstate.dialog.linetypes.traits.HasAction
import shortstate.dialog.linetypes.traits.HasReportType
import ui.MainUI
import ui.MyAnchorPane
import ui.selectionmodal.SelectionModal
import ui.selectionmodal.Tab
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

    fun inConvoPage(perspective: ShortStateCharacter): Scene{
        val actionButtons = buttons(perspective)

        val pane = GridPane()
        pane.add(conversationBackgroundImage(perspective), 0,0)
        pane.add(UtilityComponentFactory.bottomPane(actionButtons, perspective), 0, 1)
        val scene = Scene(pane, totalWidth, totalHeight)
        return scene
    }

    private fun buttons(perspective: ShortStateCharacter): List<Button> {
        var retval = mutableListOf<Button>()
        if(lineBeingConstructed != null){
            if(lineBeingConstructed is HasAction){
                retval.add(UtilityComponentFactory.shortButton("Select Action",
                    EventHandler { _ -> Controller.singleton!!.GUI!!.focusOn(
                        SelectionModal(Controller.singleton!!.GUI!!,
                            listOf(Tab("Basic Actions",Controller.singleton!!.game!!.possibleActionsForPlayer(perspective.player))),
                            { action ->
                                (lineBeingConstructed as HasAction).mySetAction(action); focusOnLine(lineBeingConstructed!!)
                            })
                    )})
                )
            }
            if(lineBeingConstructed is HasReportType){
                retval.add(UtilityComponentFactory.shortButton("Select Report",
                    EventHandler { _ -> Controller.singleton!!.GUI!!.focusOn(
                        SelectionModal(Controller.singleton!!.GUI!!,
                            listOf(Tab("Reports",Controller.singleton!!.GUI!!.playingAs().player.titles.flatMap { title -> title.reportsEntitled })),
                            { reportType ->
                                (lineBeingConstructed as HasReportType).mySetReportType(reportType); focusOnLine(lineBeingConstructed!!)
                            })
                    )})
                )
            }
            if(lineBeingConstructed!!.validToSend()){
                retval.add(UtilityComponentFactory.shortButton("Commit Line",
                    EventHandler { _ -> conversation.submitLine(lineBeingConstructed!!, Controller.singleton!!.shortThreadForPlayer(perspective).shortGame);
                        lineBeingConstructed = null; Controller.singleton!!.GUI!!.defocus()}))
            }
            retval.add(UtilityComponentFactory.shortButton(
                "Cancel",
                EventHandler { Controller.singleton!!.GUI!!.defocus()})
            )
        } else {
            if(conversation.lastLine != null
                && conversation.lastLine!!.possibleReplies().isNotEmpty()){
                retval = conversation.lastLine!!.possibleReplies()
                    .map { line -> UtilityComponentFactory.shortButton(line.tooltipName(), EventHandler {focusOnLine(line)}) }.toMutableList()
            } else {
                retval = perspective.defaultConversationLines().map{ line -> UtilityComponentFactory.shortButton(line.tooltipName(), EventHandler {focusOnLine(line)})}.toMutableList()
            }
            retval.add(UtilityComponentFactory.newSceneButton(perspective))
        }

        return retval
    }

    fun conversationBackgroundImage(perspective: ShortStateCharacter): Pane {
        val imagePane = SceneComponentFactory(Controller.singleton!!.GUI!!).sceneImage()

        if(conversation != null){
            val npcSpeechView = UtilityComponentFactory.imageView("assets//general//leftSpeechBubble.png")
            val playerSpeechView = UtilityComponentFactory.imageView("assets//general//rightSpeechBubble.png")
            imagePane.children.addAll(npcSpeechView, playerSpeechView)

            imagePane.children.add(descriptionPane(perspective))

            val lineAnchorPane = MyAnchorPane()
            linePane(lineAnchorPane, lineBeingConstructed, myLineSymbolic, true)
            linePane(lineAnchorPane, conversation.lastLine, otherLineSymbolic, false)

            imagePane.children.add(lineAnchorPane.realPane)
        }
        return imagePane
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
                playerLineText.maxWidth(totalWidth / 2)
                if (totalWidth > 600.0) {
                    playerLineText.font = Font(20.0)
                }
                playerLineText.wrappingWidth = totalWidth * 0.28
                (lineNode as GridPane).add(playerLineText, 0, index++)
            }
        } else {
            lineNode = Text(line.fullTextForm(conversation.lastSpeaker.player, conversation.otherParticipant(conversation.lastSpeaker).player))

            lineNode.maxWidth(totalWidth / 2)
            if (totalWidth > 800.0) {
                lineNode.font = Font(16.0)
            }
            lineNode.wrappingWidth = totalWidth * 0.28
        }

        if(left){
            lineNode.setOnMouseClicked { _ -> myLineSymbolic = !myLineSymbolic; Controller.singleton!!.GUI!!.display() }
        } else {
            lineNode.setOnMouseClicked { _ -> otherLineSymbolic = !otherLineSymbolic; Controller.singleton!!.GUI!!.display() }
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
        Controller.singleton!!.GUI!!.focusOn(conversation)
    }

}