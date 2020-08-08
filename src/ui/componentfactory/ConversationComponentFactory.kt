package ui.componentfactory

import shortstate.dialog.Line
import javafx.scene.Node
import javafx.scene.layout.GridPane
import javafx.scene.layout.Pane
import javafx.scene.text.Font
import javafx.scene.text.Text
import main.Controller
import main.UIGlobals
import shortstate.Conversation
import shortstate.ShortStateCharacter
import shortstate.dialog.LineBlock
import ui.MAIN_WINDOW_PORTION
import ui.MyAnchorPane


class ConversationComponentFactory {

    val conversation: Conversation
    var lineBeingConstructed: Line? = null

    var myLineSymbolic: Boolean = true
    var otherLineSymbolic: Boolean = false

    constructor(conversation: Conversation){
        this.conversation = conversation
    }

    fun conversationPane(backgroundPane: Pane, perspective: ShortStateCharacter): Pane {

        if(conversation != null){
            val npcSpeechView = UtilityComponentFactory.imageView("assets/general/leftSpeechBubble.png", MAIN_WINDOW_PORTION)
            val playerSpeechView = UtilityComponentFactory.imageView("assets/general/rightSpeechBubble.png", MAIN_WINDOW_PORTION)
            playerSpeechView.setOnMouseClicked { _ -> otherLineSymbolic = !otherLineSymbolic; UIGlobals.refresh() }
            backgroundPane.children.addAll(npcSpeechView, playerSpeechView, CharacterDetailComponentFactory.characterNameText(conversation.otherParticipant(perspective), false))

            if(lineBeingConstructed != null){
                if(lineBeingConstructed!!.validToSend()){
                    val sendButton = UtilityComponentFactory.imageView("assets/general/talkButton.png", MAIN_WINDOW_PORTION)
                    sendButton.setOnMouseClicked { _ -> conversation.submitLine(lineBeingConstructed!!, Controller.singleton!!.shortThreadForPlayer(perspective).shortGame);
                        lineBeingConstructed = null; UIGlobals.defocus()}
                    backgroundPane.children.add(sendButton)
                }
                val cancelButton = UtilityComponentFactory.imageView("assets/general/cancelLineButton.png", MAIN_WINDOW_PORTION)
                cancelButton.setOnMouseClicked { lineBeingConstructed = null; UIGlobals.refresh()}
                backgroundPane.children.add(cancelButton)
            }

            val lineAnchorPane = MyAnchorPane()
            linePane(perspective, lineAnchorPane, lineBeingConstructed, myLineSymbolic, true)
            linePane(perspective, lineAnchorPane, conversation.lastLine, otherLineSymbolic, false)

            backgroundPane.children.add(lineAnchorPane.realPane)
        }

        return backgroundPane
    }

    private fun linePane(perspective: ShortStateCharacter, pane: MyAnchorPane, line: Line?, symbolic: Boolean, left: Boolean): MyAnchorPane {
        var lineNode: Node? = null
        var game = UIGlobals.activeShortGame()
        if(symbolic){
            lineNode = GridPane()

            val textBlocks = if(line != null){
                line.symbolicForm( game, conversation.otherParticipant(conversation.lastSpeaker), conversation.lastSpeaker)
            } else if(left){
                lineOptions(perspective).map { line -> LineBlock(line.tooltipName(), line.tooltipDescription(), {focusOnLine(line); UIGlobals.refresh()}) }
            } else {
                listOf()
            }
            var index = 0 //gotta be a better way to do this
            textBlocks.forEach { block ->
                val playerLineText = block.textForm(perspective)
                playerLineText.maxWidth(UIGlobals.totalWidth() / 2)
                if (UIGlobals.totalWidth() > 600.0) {
                    playerLineText.font = Font(20.0)
                }
                playerLineText.wrappingWidth = UIGlobals.totalWidth() * 0.28
                if(block.tooltip != null && block.tooltip != ""){
                    UtilityComponentFactory.applyTooltip(playerLineText, block.tooltip)
                }
                (lineNode as GridPane).add(playerLineText, 0, index++)

                if(block.behavior==null){
                    if(!left) {
                        playerLineText.setOnMouseClicked { _ -> otherLineSymbolic = !otherLineSymbolic; UIGlobals.refresh() }
                    }
                }
            }
        } else {
            if(line != null){
                lineNode = Text(line.fullTextForm(game, conversation.lastSpeaker, conversation.otherParticipant(conversation.lastSpeaker)))

                lineNode.maxWidth(UIGlobals.totalWidth() / 2)
                if (UIGlobals.totalWidth() > 800.0) {
                    lineNode.font = Font(16.0)
                }
                lineNode.wrappingWidth = UIGlobals.totalWidth() * 0.28

                if(!left) {
                    lineNode.setOnMouseClicked { _ -> otherLineSymbolic = !otherLineSymbolic; UIGlobals.refresh() }
                }
            } else {
                return pane
            }
        }

        pane.realPane.children.add(lineNode)

        pane.setTopAnchor(lineNode, UIGlobals.totalHeight() * 0.03);
        if(left){
            pane.setLeftAnchor(lineNode, UIGlobals.totalWidth() * 0.03);
        } else {
            pane.setLeftAnchor(lineNode, UIGlobals.totalWidth() * 0.693);
        }

        return pane
    }

    private fun lineOptions(perspective: ShortStateCharacter): List<Line>{
        var retval = listOf<Line>()
        if(conversation.lastLine != null
            && conversation.lastLine!!.possibleReplies(perspective).isNotEmpty()){
            retval = conversation.lastLine!!.possibleReplies(perspective)
        }
        if(conversation.lastLine == null || conversation.lastLine!!.canChangeTopic() || retval.size == 0){
            retval = retval.plus(conversation.defaultConversationLines(perspective))
        }
        retval = retval.plus(perspective.player.specialLines.filter{it.shouldGenerateLine(UIGlobals.activeGame(), conversation.lastLine, perspective, conversation.otherParticipant(perspective))}
            .map { it.generateLine(UIGlobals.activeGame(), conversation.lastLine, perspective, conversation.otherParticipant(perspective)) })
        return retval
    }

    fun focusOnLine(line: Line){
        lineBeingConstructed = line
    }

}