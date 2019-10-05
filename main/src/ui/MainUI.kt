package ui

import dialog.Line
import javafx.application.Application;
import javafx.event.EventHandler
import javafx.stage.Stage
import javafx.scene.Scene
import javafx.scene.layout.FlowPane
import javafx.scene.layout.GridPane
import javafx.scene.layout.Pane
import javafx.stage.WindowEvent
import main.Controller
import shortstate.ShortStateGame
import shortstate.ShortStatePlayer
import java.lang.Exception
import kotlin.system.exitProcess

const val SIZE_SCALE = 0.8

class MainUI() : Application() {

    private var stage: Stage? = null

    var generalComponents = GeneralComponentFactory(this)
    var sceneComponents = SceneComponentFactory(this)
    var conversationComponents = ConversationComponentFactory(this)

    var totalWidth = 1200.0 * SIZE_SCALE
    var totalHeight = 1080.0 * SIZE_SCALE

    var scene: shortstate.Scene? = null
    var lineBeingConstructed: Line? = null
    var actionSelectModal: ActionSelectModal? = null
    var roomSelectModal: RoomSelectModal? = null

    private var curFocus = Focus.SCENE

    private enum class Focus{
        GENERAL, SCENE, LINE, ACTION_SELECT_MODAL, ROOM_SELECT_MODAL
    }

    override fun start(primaryStage: Stage) {
        primaryStage.onCloseRequest = EventHandler<WindowEvent> { exitProcess(0) }
        stage = primaryStage
        focusOn(Controller.singleton!!.sceneForPlayer(playingAs()))
        display()
    }

    fun shortGame(): ShortStateGame {
        return Controller.singleton!!.shortGame!!
    }

    fun playingAs(): ShortStatePlayer {
        return shortGame().playerCharacter()
    }

    fun focusOn(focus: Any?){
        if(focus == null){
            scene = null
            lineBeingConstructed = null
            curFocus = Focus.GENERAL
        } else if(focus is shortstate.Scene){
            scene = focus
            lineBeingConstructed = null
            actionSelectModal = null
            curFocus = Focus.SCENE
        } else if(focus is Line){
            lineBeingConstructed = focus
            actionSelectModal = null
            curFocus = Focus.LINE
        } else if(focus is ActionSelectModal){
            actionSelectModal = focus
            curFocus = Focus.ACTION_SELECT_MODAL
        } else if(focus is RoomSelectModal){
            roomSelectModal = focus
            curFocus = Focus.ROOM_SELECT_MODAL
        } else {
            throw Exception("INVALID TYPE FOR FOCUS! "+focus.javaClass)
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
        } else if(curFocus == Focus.SCENE){
           setScene(sceneComponents.scenePage())
        } else{
            throw Exception("GENERAL Scope not implemented yet!")
        }

        this.stage!!.show()
    }

    fun setScene(scene: Scene){
        this.stage!!.scene = scene
    }

    fun establishConversation(){
        val convo = Controller.singleton!!.createConversation(playingAs(), Controller.singleton!!.shortGame!!.players[1], playingAs().player.location.startRoom())
        if(convo != null){
            focusOn(convo)
        }
        display()
    }


}
