package ui

import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import main.Controller

class ConversationComponentFactory {

    val parent: MainUI

    constructor(parent: MainUI){
        this.parent = parent
    }

    fun announceOptions(): Scene {
        val buttonsPane = GridPane()
        val btn1 = parent.generalComponents.makeShortButton("I'm gonna bake a cookie!", EventHandler { _ -> println("yup that works") })
        buttonsPane.add(btn1, 0, 0)
        val btn2 = parent.generalComponents.makeShortButton( "filler", null)
        buttonsPane.add(btn2, 1, 0)
        val btn3 = parent.generalComponents.makeShortButton("filler", null)
        buttonsPane.add(btn3, 2, 0)
        val btn4 = parent.generalComponents.makeShortButton("filler", null)
        buttonsPane.add(btn4, 3, 0)
        val btn5 = parent.generalComponents.makeShortButton("filler", null)
        buttonsPane.add(btn5, 0, 1)
        val btn6 = parent.generalComponents.makeShortButton("filler", null)
        buttonsPane.add(btn6, 1, 1)
        val btn7 = parent.generalComponents.makeShortButton("filler", null)
        buttonsPane.add(btn7, 2, 1)
        val btn8 = parent.generalComponents.makeShortButton(
            "Cancel",
            EventHandler { parent.setFocus(parent.conversation); parent.display() })
        buttonsPane.add(btn8, 3, 1)

        val imagePane = parent.generalComponents.mainImage()
        val npcSpeechView = parent.generalComponents.makeImageView("assets//NPCSpeachBubble.png")
        npcSpeechView.setOnMouseClicked { _ -> println("heyo!") }
        val playerSpeechView = parent.generalComponents.makeImageView("assets//PlayerSpeechBubble.png")
        imagePane.children.addAll(npcSpeechView, playerSpeechView)

        val pane = GridPane()
        pane.add(imagePane, 0, 0)
        pane.add(buttonsPane, 0, 1)
        val scene = Scene(pane, parent.totalWidth, parent.totalHeight)
        return scene
    }
}