package ui

import dialog.Line
import dialog.linetypes.Announcement
import game.Conversation
import javafx.application.Application;
import javafx.event.EventHandler
import javafx.stage.Stage
import javafx.scene.Scene
import javafx.scene.layout.FlowPane
import javafx.scene.layout.GridPane
import javafx.scene.layout.Pane
import javafx.stage.WindowEvent
import main.Controller
import kotlin.system.exitProcess

const val SIZE_SCALE = 0.8

class MainUI() : Application() {

    private var stage: Stage? = null

    var generalComponents = GeneralComponentFactory(this)
    var conversationComponents = ConversationComponentFactory(this)

    var totalWidth = 1200.0 * SIZE_SCALE
    var totalHeight = 1080.0 * SIZE_SCALE

    var conversation: Conversation? = null
    var line: Line? = null

    override fun start(primaryStage: Stage) {
        primaryStage.onCloseRequest = EventHandler<WindowEvent> { exitProcess(0) }
        stage = primaryStage

        display()
    }

    fun setFocus(focus: Any?){
        if(focus == null){
            conversation = null
            line = null
        }
        if(focus is Conversation){
            conversation = focus
            line = null
        }
        if(focus is Line){
            line = focus
        }
    }

    fun display(){
        if(line != null) {
            setScene(conversationComponents.announceOptions())
        } else if(conversation != null){
           setScene(inConvoPage())
        } else {
            setScene(outOfConvoPage())
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

    fun inConvoPage(): Scene{
        val buttonsPane = GridPane()
        val btn1 = generalComponents.makeShortButton("Announce", EventHandler { line = Announcement(null); display() })
        buttonsPane.add(btn1, 0,0)
        val btn2 = generalComponents.makeShortButton("filler", null)
        buttonsPane.add(btn2, 1,0)
        val btn3 = generalComponents.makeShortButton("filler", null)
        buttonsPane.add(btn3, 2,0)
        val btn4 = generalComponents.makeShortButton("filler", null)
        buttonsPane.add(btn4, 3,0)
        val btn5 = generalComponents.makeShortButton("filler", null)
        buttonsPane.add(btn5, 0,1)
        val btn6 = generalComponents.makeShortButton("filler", null)
        buttonsPane.add(btn6, 1,1)
        val btn7 = generalComponents.makeShortButton("filler", null)
        buttonsPane.add(btn7, 2,1)
        val btn8 = generalComponents.makeShortButton("Cancel", EventHandler{ _ -> Controller.singleton!!.endConversation(conversation!!); setFocus(null); display()})
        buttonsPane.add(btn8, 3,1)

        val pane = GridPane()
        pane.add(conversationComponents.conversationBackgroundImage(), 0,0)
        pane.add(buttonsPane, 0, 1)
        val scene = Scene(pane, totalWidth, totalHeight)
        return scene
    }

    private fun establishConversation(){
        setFocus(Controller.singleton!!.createConversation(Controller.singleton!!.game!!.playerCharacter(), Controller.singleton!!.game!!.players[1]))
        display()
    }

    private fun outOfConvoButtons(): Pane {
        val bottomButtonsPanel = FlowPane()
        val btn1 = generalComponents.makeTallButton("Converse", EventHandler{ _ -> establishConversation() })
        val btn2 = generalComponents.makeTallButton("Filler 1", null)
        val btn3 = generalComponents.makeTallButton("Filler 2", null)
        bottomButtonsPanel.children.add(btn1)
        bottomButtonsPanel.children.add(btn2)
        bottomButtonsPanel.children.add(btn3)
        return bottomButtonsPanel
    }


}
