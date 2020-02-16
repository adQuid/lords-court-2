package ui

import shortstate.dialog.Line
import javafx.application.Application;
import javafx.event.EventHandler
import javafx.stage.Stage
import javafx.scene.Scene
import javafx.stage.WindowEvent
import main.Controller
import shortstate.Conversation
import shortstate.ShortStateGame
import shortstate.ShortStateCharacter
import shortstate.ShortStateController
import ui.componentfactory.*
import ui.selectionmodal.SelectionModal
import java.lang.Exception
import kotlin.system.exitProcess

const val SIZE_SCALE = 0.8

var totalWidth = 1200.0 * SIZE_SCALE
var totalHeight = 1080.0 * SIZE_SCALE

class MainUI() : Application() {

    private var stage: Stage? = null

    var sceneComponents = SceneComponentFactory(this)
    var nonSceneComponents = WaitingSceneComponentFactory(this)

    var totalWidth = 1200.0 * SIZE_SCALE
    var totalHeight = 1080.0 * SIZE_SCALE

    var shortGameScene: shortstate.ShortGameScene? = null
    var conversation: Conversation? = null
    var selectModal: SelectionModal<*>? = null
    var character: ShortStateCharacter? = null

    private var lastFocus: Focus = Focus.GENERAL
    private var curFocus = Focus.GENERAL

    private enum class Focus{
        GENERAL, SCENE, CONVERSATION, CHARACTER, SELECT_MODAL
    }

    override fun init(){
        Controller.singleton!!.registerUI(this)
    }

    override fun start(primaryStage: Stage) {
        primaryStage.onCloseRequest = EventHandler<WindowEvent> { exitProcess(0) }
        stage = primaryStage
        refocus()

        /* still working on scaling with this
        stage!!.widthProperty().addListener({_ -> totalHeight = stage!!.height; totalWidth = stage!!.width; })
        stage!!.heightProperty().addListener({_ -> totalHeight = stage!!.height; totalWidth = stage!!.width; })*/

        display()

    }

    fun shortThread(): ShortStateController {
        return Controller.singleton!!.shortThread!!
    }

    fun shortGame(): ShortStateGame {
        return Controller.singleton!!.shortThread!!.shortGame
    }

    fun playingAs(): ShortStateCharacter {
        return shortGame().playerCharacter()
    }

    fun focusOn(focus: Any?){
        if(curFocus != null && curFocus != Focus.SELECT_MODAL){
            lastFocus = curFocus
        }
        if(focus == null){
            shortGameScene = null
            conversation = null
            curFocus = Focus.GENERAL
        } else if(focus is shortstate.ShortGameScene){
            shortGameScene = focus
            conversation = null
            selectModal = null
            curFocus = Focus.SCENE
        } else if(focus is Conversation){
            conversation = focus
            selectModal = null
            curFocus = Focus.CONVERSATION
        } else if(focus is SelectionModal<*>){
            selectModal = focus
            curFocus = Focus.SELECT_MODAL
        } else if(focus is ShortStateCharacter){
            character = focus
            curFocus = Focus.CHARACTER
        } else {
            throw Exception("INVALID TYPE FOR FOCUS! "+focus.javaClass)
        }
        display()
    }

    //focuses on whatever the scene is at this point
    fun refocus(){
        focusOn(shortGame().sceneForPlayer(playingAs()))
    }

    fun defocus(){
        curFocus = lastFocus
        lastFocus = Focus.GENERAL
        display()
    }

    fun display(){
        if(curFocus == Focus.CHARACTER){
            setScene(character!!.display(playingAs()))
        }else if(curFocus == Focus.SELECT_MODAL){
            setScene(selectModal!!.getScene())
        }else if(curFocus == Focus.CONVERSATION) {
            setScene(sceneComponents.scenePage(playingAs()))
        } else if(curFocus == Focus.SCENE){
            setScene(sceneComponents.scenePage(playingAs()))
        } else{
            setScene(nonSceneComponents.waitingPage(playingAs()))
        }

        this.stage!!.show()
    }

    fun setScene(scene: Scene){
        this.stage!!.scene = scene
    }

}
