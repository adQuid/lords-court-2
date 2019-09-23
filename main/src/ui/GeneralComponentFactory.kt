package ui

import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.Pane
import javafx.scene.layout.StackPane

class GeneralComponentFactory {

    val parent: MainUI

    constructor(parent: MainUI){
        this.parent = parent
    }

    fun mainImage(): Pane {
        val backgroundView = makeImageView("assets//rooms//test.png")
        val characterView = makeImageView("assets//portraits//person.png")
        val imagePane = StackPane()
        imagePane.children.addAll(backgroundView, characterView)
        return imagePane
    }

    fun makeImageView(url: String): ImageView {
        val image = Image(url)
        val retval = ImageView(image)
        retval.fitHeight = (parent.totalHeight) * (5.0 / 6.0)
        retval.fitWidth = parent.totalWidth
        return retval
    }

    fun makeTallButton(
        text: String,
        action: EventHandler<ActionEvent>?
    ): Button {
        val retval = Button(text)
        retval.setMinSize(parent.totalWidth / 4, parent.totalHeight / 6)
        if (action != null) {
            retval.onAction = action
        }
        return retval
    }

    fun makeShortButton(
        text: String,
        action: EventHandler<ActionEvent>?
    ): Button {
        val retval = Button(text)
        retval.setMinSize(parent.totalWidth / 4, parent.totalHeight / 12)
        if (action != null) {
            retval.onAction = action
        }
        return retval
    }
}