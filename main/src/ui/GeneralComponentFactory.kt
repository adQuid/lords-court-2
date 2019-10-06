package ui

import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.Pane
import javafx.scene.layout.StackPane
import main.Controller
import ui.selectionmodal.SelectionModal

class GeneralComponentFactory {

    val parent: MainUI

    constructor(parent: MainUI){
        this.parent = parent
    }

    fun sceneImage(): Pane {
        val imagePane = StackPane()
        val backgroundView: ImageView
        if(parent.scene?.characters!!.size > 1){
            backgroundView = makeImageView(parent.scene!!.room.pictureText)
            val characterView = makeImageView("assets//portraits//faceman.png")
            imagePane.children.addAll(backgroundView, characterView)
        } else {
            backgroundView = makeImageView(parent.scene!!.room.pictureText)
            imagePane.children.addAll(backgroundView)
        }
        return imagePane
    }

    fun makeImageView(url: String): ImageView {
        val image = Image(url)
        val retval = ImageView(image)
        retval.fitHeight = (parent.totalHeight) * (5.0 / 6.0)
        retval.fitWidth = parent.totalWidth
        return retval
    }

    fun makeTallButton(text: String, action: EventHandler<ActionEvent>?): Button {
        return makeTall(makeShortButton(text, action))
    }

    fun makeTall(button: Button): Button{
        button.setMinSize(parent.totalWidth / 4, parent.totalHeight / 6)
        return button
    }

    fun makeShortButton(text: String, action: EventHandler<ActionEvent>?): Button {
        val retval = Button(text)
        retval.setMinSize(parent.totalWidth / 4, parent.totalHeight / 12)
        if (action != null) {
            retval.onAction = action
        }
        return retval
    }

    fun makeShortWideButton(text: String, action: EventHandler<ActionEvent>?): Button {
        val retval = Button(text)
        retval.setMinSize(parent.totalWidth, parent.totalHeight / 12)
        if (action != null) {
            retval.onAction = action
        }
        return retval
    }

}