package ui.componentfactory

import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.GridPane
import javafx.scene.layout.Pane
import main.Controller
import ui.MainUI

class UtilityComponentFactory {

    val parent: MainUI

    constructor(parent: MainUI){
        this.parent = parent
    }

    fun sceneImage(): Pane {
        val imagePane = Pane()
        val backgroundView: ImageView
        if(parent.scene?.characters!!.size > 1){
            val otherPlayer = parent.scene!!.conversation!!.otherParticipant(parent.playingAs())
            backgroundView = imageView(parent.scene!!.room.pictureText)
            val characterView = imageView(otherPlayer.player.pictureString)
            characterView.setOnMouseClicked { event -> parent.focusOn(otherPlayer.player) }
            imagePane.children.addAll(backgroundView, characterView)
        } else {
            backgroundView = imageView(parent.scene!!.room.pictureText)
            imagePane.children.addAll(backgroundView)
        }
        return imagePane
    }

    fun imageView(url: String): ImageView {
        val image = Image(url)
        val retval = ImageView(image)
        retval.fitHeight = (parent.totalHeight) * (4.5 / 6.0)
        retval.fitWidth = parent.totalWidth
        return retval
    }

    fun bottomPane(buttons: List<Button>): Pane{

        val buttonsPane = GridPane()
        for(index in 0..7){
            if(index < buttons.size){
                buttonsPane.add(buttons[index], (index % 4), if(index > 3) 1 else 0)
            } else {
                buttonsPane.add(shortButton("", null), (index % 4), if(index > 3) 1 else 0)
            }
        }

        val retval = GridPane()
        retval.add(middlePane(), 0, 0)
        retval.add(buttonsPane, 0,1)

        return retval
    }

    private fun middlePane(): Pane{
        val retval = GridPane()

        val statsDisplay = Label("Energy: " + parent.playingAs().energy + "/1000")
        statsDisplay.setMinSize(parent.totalWidth/4, parent.totalWidth / 12)
        retval.add(statsDisplay, 0,0)

        val saveButton = shortButton("Save", EventHandler { Controller.singleton!!.save()})
        retval.add(saveButton, 1,0)

        return retval
    }

    fun shortButton(text: String, action: EventHandler<ActionEvent>?): Button {
        val retval = Button(text)
        retval.setMinSize(parent.totalWidth / 4, parent.totalHeight / 12)
        if (action != null) {
            retval.onAction = action
        }
        return retval
    }

    fun shortWideButton(text: String, action: EventHandler<ActionEvent>?): Button {
        val retval = Button(text)
        retval.setMinSize(parent.totalWidth, parent.totalHeight / 12)
        if (action != null) {
            retval.onAction = action
        }
        return retval
    }

}