package ui

import shortstate.dialog.Line
import javafx.application.Application;
import javafx.event.EventHandler
import javafx.stage.Stage
import javafx.scene.Scene
import javafx.stage.WindowEvent
import main.Controller
import shortstate.ShortStateGame
import shortstate.ShortStateCharacter
import ui.selectionmodal.SelectionModal
import java.lang.Exception
import kotlin.system.exitProcess

const val SIZE_SCALE = 0.8

class MainUI() : Application() {

    private var stage: Stage? = null

    var generalComponents = GeneralComponentFactory(this)
    var sceneComponents = SceneComponentFactory(this)
    var conversationComponents = ConversationComponentFactory(this)
    var nonSceneComponents = NonSceneComponentFactory(this)
    var characterDetailComponents = CharacterDetailComponentFactory(this)

    var totalWidth = 1200.0 * SIZE_SCALE
    var totalHeight = 1080.0 * SIZE_SCALE

    var scene: shortstate.Scene? = null
    var lineBeingConstructed: Line? = null
    var selectModal: SelectionModal<*>? = null
    var character: game.Character? = null

    private var lastFocus: Focus = Focus.GENERAL
    private var curFocus = Focus.GENERAL

    private enum class Focus{
        GENERAL, SCENE, LINE, CHARACTER, SELECT_MODAL
    }

    override fun init(){
        Controller.singleton!!.registerUI(this)
    }

    override fun start(primaryStage: Stage) {
        primaryStage.onCloseRequest = EventHandler<WindowEvent> { exitProcess(0) }
        stage = primaryStage
        focusOn(Controller.singleton!!.sceneForPlayer(playingAs()))

        /* still working on scaling with this
        stage!!.widthProperty().addListener({_ -> totalHeight = stage!!.height; totalWidth = stage!!.width; })
        stage!!.heightProperty().addListener({_ -> totalHeight = stage!!.height; totalWidth = stage!!.width; })*/

        display()

    }

    fun shortGame(): ShortStateGame {
        return Controller.singleton!!.shortGame!!
    }

    fun playingAs(): ShortStateCharacter {
        return shortGame().playerCharacter()
    }

    fun focusOn(focus: Any?){
        if(curFocus != null){
            lastFocus = curFocus
        }
        if(focus == null){
            scene = null
            lineBeingConstructed = null
            curFocus = Focus.GENERAL
        } else if(focus is shortstate.Scene){
            scene = focus
            lineBeingConstructed = null
            selectModal = null
            curFocus = Focus.SCENE
        } else if(focus is Line){
            lineBeingConstructed = focus
            selectModal = null
            curFocus = Focus.LINE
        } else if(focus is SelectionModal<*>){
            selectModal = focus
            curFocus = Focus.SELECT_MODAL
        } else if(focus is game.Character){
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
            setScene(characterDetailComponents.characterFocusPage())
        }else if(curFocus == Focus.SELECT_MODAL){
            setScene(selectModal!!.getScene())
        }else if(curFocus == Focus.LINE) {
            setScene(conversationComponents.announceOptions())
        } else if(curFocus == Focus.SCENE){
           setScene(sceneComponents.scenePage())
        } else{
           setScene(nonSceneComponents.waitingPage())
        }

        this.stage!!.show()
    }

    fun setScene(scene: Scene){
        this.stage!!.scene = scene
    }

}
