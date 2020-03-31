package ui

import javafx.application.Application;
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.stage.Stage
import javafx.scene.Scene
import javafx.stage.WindowEvent
import main.Controller
import shortstate.ShortStateGame
import shortstate.ShortStateCharacter
import ui.componentfactory.*
import java.util.*
import kotlin.system.exitProcess

var SIZE_SCALE = 0.8

class MainUI() : Application() {

    private var stage: Stage? = null

    var totalWidth = 1200.0 * SIZE_SCALE
    var totalHeight = 1080.0 * SIZE_SCALE

    var curFocus: Stack<Displayable?> = Stack<Displayable?>()

    override fun init(){
        Controller.singleton!!.registerUI(this)
    }

    override fun start(primaryStage: Stage) {
        primaryStage.onCloseRequest = EventHandler<WindowEvent> { exitProcess(0) }
        stage = primaryStage
        resetFocus()

        display()
    }

    fun shortGame(): ShortStateGame {
        return Controller.singleton!!.shortThread!!.shortGame
    }

    fun playingAs(): ShortStateCharacter {
        return shortGame().playerCharacter()
    }

    fun focusOn(focus: Displayable?){
        if(focus == null){
            curFocus.clear()
        } else {
            curFocus.push(focus)
        }
        display()
    }

    //focuses on whatever the scene is at this point
    fun resetFocus(){
        focusOn(shortGame().sceneForPlayer(playingAs()))
    }

    fun defocus(){
        curFocus.pop()
        display()
    }

    fun display(){
        if(curFocus.size > 0){
            setScene(curFocus.peek()!!.display(playingAs()))
        }else{
            setScene(WaitingSceneComponentFactory().waitingPage(playingAs()))
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

        totalWidth = 1200.0 * SIZE_SCALE
        totalHeight = 1080.0 * SIZE_SCALE
        display()
    }
}
