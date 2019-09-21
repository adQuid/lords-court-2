package ui

import game.Conversation
import game.Player
import javafx.application.Application;
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.FlowPane
import javafx.scene.layout.GridPane
import javafx.scene.layout.Pane
import javafx.scene.layout.StackPane
import javafx.stage.WindowEvent
import main.Controller
import java.lang.System.exit
import kotlin.system.exitProcess

class MainUI() : Application() {

    var stage: Stage? = null

    var totalWidth = 800.0
    var totalHeight = 600.0

    var conversation: Conversation? = null

    override fun start(primaryStage: Stage) {
        primaryStage.onCloseRequest = EventHandler<WindowEvent> { exitProcess(0) }
        stage = primaryStage

        display()
    }

    fun display(){
        if(conversation != null){
            this.stage!!.scene = inConvoPage()
        } else {
            this.stage!!.scene = outOfConvoPage()
        }


        this.stage!!.show()
    }

    fun outOfConvoPage(): Scene{
        val root = GridPane()
        root.add(outOfConvoButtons(), 0, 1)
        root.add(mainImage(), 0, 0)
        val scene = Scene(root, totalWidth, totalHeight)
        return scene
    }

    fun inConvoPage(): Scene{

        val buttonsPane = GridPane()
        val btn1 = makeShortButton("Request", null)
        buttonsPane.add(btn1, 0,0)
        val btn2 = makeShortButton("filler", null)
        buttonsPane.add(btn2, 1,0)
        val btn3 = makeShortButton("filler", null)
        buttonsPane.add(btn3, 2,0)
        val btn4 = makeShortButton("filler", null)
        buttonsPane.add(btn4, 3,0)
        val btn5 = makeShortButton("filler", null)
        buttonsPane.add(btn5, 0,1)
        val btn6 = makeShortButton("filler", null)
        buttonsPane.add(btn6, 1,1)
        val btn7 = makeShortButton("filler", null)
        buttonsPane.add(btn7, 2,1)
        val btn8 = makeShortButton("Cancel", EventHandler{ _ -> this.conversation = null; display()})
        buttonsPane.add(btn8, 3,1)


        val imagePane = mainImage()
        val npcSpeechView = makeImageView("assets//NPCSpeachBubble.png")
        npcSpeechView.setOnMouseClicked{_ -> println("heyo!")}
        val playerSpeechView = makeImageView("assets//PlayerSpeechBubble.png")
        imagePane.children.addAll(npcSpeechView, playerSpeechView)

        val pane = GridPane()
        pane.add(imagePane, 0,0)
        pane.add(buttonsPane, 0, 1)
        val scene = Scene(pane, totalWidth, totalHeight)
        return scene
    }

    private fun establishConversation(){
        conversation = Controller.singleton!!.createConversation(Controller.singleton!!.game!!.playerCharacter(), Controller.singleton!!.game!!.players[1])

        display()
    }

    private fun outOfConvoButtons(): Pane {
        val bottomButtonsPanel = FlowPane()
        val btn1 = makeTallButton("Converse", EventHandler{ _ -> establishConversation() })
        val btn2 = makeTallButton("Filler 1", null)
        val btn3 =  makeTallButton("Filler 2", null)
        bottomButtonsPanel.children.add(btn1)
        bottomButtonsPanel.children.add(btn2)
        bottomButtonsPanel.children.add(btn3)
        return bottomButtonsPanel
    }

    private fun mainImage(): Pane {
        val backgroundView = makeImageView("assets//test.png")
        val characterView = makeImageView("assets//person.png")
        val imagePane = StackPane()
        imagePane.children.addAll(backgroundView, characterView)
        return imagePane
    }

    private fun makeImageView(url: String): ImageView {
        val image = Image(url)
        val retval = ImageView(image)
        retval.fitHeight = (this.totalHeight) * (5.0/6.0)
        retval.fitWidth = this.totalWidth
        return retval
    }

    private fun makeTallButton(text: String, action: EventHandler<ActionEvent>?): Button {
        val retval = Button(text)
        retval.setMinSize(this.totalWidth/4,this.totalHeight/6)
        if (action != null) {
            retval.onAction = action
        }
        return retval
    }

    private fun makeShortButton(text: String, action: EventHandler<ActionEvent>?): Button {
        val retval = Button(text)
        retval.setMinSize(this.totalWidth/4,this.totalHeight/12)
        if (action != null) {
            retval.onAction = action
        }
        return retval
    }
}
