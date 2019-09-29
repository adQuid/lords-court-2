package ui

import dialog.Line
import game.Conversation
import game.Player
import javafx.application.Application;
import javafx.event.EventHandler
import javafx.stage.Stage
import javafx.scene.Scene
import javafx.scene.layout.FlowPane
import javafx.scene.layout.GridPane
import javafx.scene.layout.Pane
import javafx.stage.WindowEvent
import main.Controller
import shortstate.Room
import java.lang.Exception
import kotlin.system.exitProcess

const val SIZE_SCALE = 0.8

class MainUI() : Application() {

    private var stage: Stage? = null

    var generalComponents = GeneralComponentFactory(this)
    var conversationComponents = ConversationComponentFactory(this)

    var totalWidth = 1200.0 * SIZE_SCALE
    var totalHeight = 1080.0 * SIZE_SCALE

    var room: Room? = playingAs().location.startRoom()
    var conversation: Conversation? = null
    var lineBeingConstructed: Line? = null
    var actionSelectModal: ActionSelectModal? = null
    var roomSelectModal: RoomSelectModal? = null

    private var curFocus = Focus.ROOM

    private enum class Focus{
        GENERAL, ROOM, CONVERSATION, LINE, ACTION_SELECT_MODAL, ROOM_SELECT_MODAL
    }

    override fun start(primaryStage: Stage) {
        primaryStage.onCloseRequest = EventHandler<WindowEvent> { exitProcess(0) }
        stage = primaryStage

        display()
    }

    fun playingAs(): Player{
        return Controller.singleton!!.game!!.playerCharacter()
    }

    fun focusOn(focus: Any?){
        if(focus == null){
            conversation = null
            lineBeingConstructed = null
            curFocus = Focus.GENERAL
        }
        if(focus is Room){
            room = focus
            conversation = null
            lineBeingConstructed = null
            curFocus = Focus.ROOM
        }
        if(focus is Conversation){
            conversation = focus
            lineBeingConstructed = null
            actionSelectModal = null
            curFocus = Focus.CONVERSATION
        }
        if(focus is Line){
            lineBeingConstructed = focus
            actionSelectModal = null
            curFocus = Focus.LINE
        }

        if(focus is ActionSelectModal){
            actionSelectModal = focus
            curFocus = Focus.ACTION_SELECT_MODAL
        }
        if(focus is RoomSelectModal){
            roomSelectModal = focus
            curFocus = Focus.ROOM_SELECT_MODAL
        }
        display()
    }

    fun display(){
        if(curFocus == Focus.ROOM_SELECT_MODAL){
            setScene(roomSelectModal!!.getScene())
        }else if(curFocus == Focus.ACTION_SELECT_MODAL){
            setScene(actionSelectModal!!.getScene())
        }else if(curFocus == Focus.LINE) {
            setScene(conversationComponents.announceOptions())
        } else if(curFocus == Focus.CONVERSATION){
           setScene(conversationComponents.inConvoPage())
        } else if(curFocus == Focus.ROOM){
            setScene(outOfConvoPage())
        } else{
            throw Exception("GENERAL Scope not implemented yet!")
        }

        this.stage!!.show()
    }

    fun setScene(scene: Scene){
        this.stage!!.scene = scene
    }

    fun outOfConvoPage(): Scene{
        val root = GridPane()
        root.add(outOfConvoButtons(), 0, 1)
        root.add(generalComponents.mainImage(), 0, 0)
        val scene = Scene(root, totalWidth, totalHeight)
        return scene
    }

    private fun establishConversation(){
        focusOn(Controller.singleton!!.createConversation(playingAs(), Controller.singleton!!.game!!.players[1]))
        display()
    }

    private fun outOfConvoButtons(): Pane {
        val bottomButtonsPanel = FlowPane()
        val btn1 = generalComponents.makeTallButton("Converse", EventHandler{ _ -> establishConversation() })
        val btn2 = generalComponents.makeTallButton("Go Somewhere Else", EventHandler { _ -> focusOn(RoomSelectModal(this, {room -> focusOn(room)}) )})
        val btn3 = generalComponents.makeTallButton("Filler 2", null)
        bottomButtonsPanel.children.add(btn1)
        bottomButtonsPanel.children.add(btn2)
        bottomButtonsPanel.children.add(btn3)
        return bottomButtonsPanel
    }
}
