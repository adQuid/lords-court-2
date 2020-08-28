package ui

import javafx.application.Application;
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.scene.Node
import javafx.stage.Stage
import javafx.scene.Scene
import javafx.stage.WindowEvent
import main.Controller
import shortstate.ShortStateGame
import shortstate.ShortStateCharacter
import ui.componentfactory.*
import ui.specialdisplayables.MainMenu
import java.util.*
import kotlin.system.exitProcess

var SIZE_SCALE = 0.8
var BASE_WIDTH = 1200.0
var BASE_HEIGHT = 800.0 //900.0
const val MAIN_WINDOW_PORTION = 0.9
const val BOTTOM_BAR_PORTION = 0.1

class MainUI() : Application() {

    private var stage: Stage? = null

    var totalWidth = BASE_WIDTH * SIZE_SCALE
    var totalHeight = BASE_HEIGHT * SIZE_SCALE

    var curFocus: Stack<Displayable> = Stack()
    var specialFocus: Stack<Displayable> = Stack()
    var lastButtonClicked: Node? = null

    override fun init(){
        Controller.singleton!!.registerUI(this)
    }

    override fun start(primaryStage: Stage) {
        primaryStage.onCloseRequest = EventHandler<WindowEvent> { exitProcess(0) }
        stage = primaryStage
        focusOnMainMenu()
    }

    fun shortGame(): ShortStateGame {
        return Controller.singleton!!.playerThread().shortGame
    }

    fun playingAs(): ShortStateCharacter? {
        if(Controller.singleton!!.game == null){
            return null
        } else {
            return shortGame().playerCharacter()
        }
    }

    fun focusOn(focus: Displayable?){
        if(focus == null || focus is MainMenu){
            curFocus.clear()
        }
        if(focus != null){
            curFocus.push(focus)
        }
        display()
    }

    fun specialFocusOn(focus: Displayable?){
        specialFocus.push(focus)
    }

    fun focusOnMainMenu(){
        focusOn(MainMenu())
    }

    //focuses on whatever the scene is at this point
    fun resetFocus(){
        curFocus.clear()
        focusOn(shortGame().sceneForPlayer(playingAs()!!))
    }

    fun defocus(){
        if(specialFocus.isNotEmpty()){
            specialFocus.pop()
        }else if(curFocus.isNotEmpty()){
            curFocus.pop()
        }
        display()
    }

    fun display(){
        if(specialFocus.isNotEmpty()){
            setScene(specialFocus.peek().universalDisplay(playingAs()))
        }else if(curFocus.size > 0){
            setScene(curFocus.peek().universalDisplay(playingAs()))
        }else{
            setScene(WaitingSceneComponentFactory().waitingPage(playingAs()!!))
        }

        Platform.runLater {
            this.stage!!.show()
        }
    }

    fun setScene(scene: Scene){
        Platform.runLater {
            this.stage!!.scene = scene
        }
    }

    fun rotateSize(direction: Int){
        if(direction > 0 && SIZE_SCALE < 1.1){
            SIZE_SCALE += 0.1
        } else if(direction < 0 && SIZE_SCALE > 0.6){
            SIZE_SCALE -= 0.1
        }

        totalWidth = BASE_WIDTH * SIZE_SCALE
        totalHeight = BASE_HEIGHT * SIZE_SCALE
        display()
    }

    fun setResolution(width: Double, height: Double){
        BASE_WIDTH = width
        BASE_HEIGHT = height

        totalWidth = BASE_WIDTH * SIZE_SCALE
        totalHeight = BASE_HEIGHT * SIZE_SCALE
        display()
    }
}
