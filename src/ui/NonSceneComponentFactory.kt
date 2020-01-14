package ui

import javafx.scene.Scene
import javafx.scene.layout.GridPane
import javafx.scene.layout.Pane
import javafx.scene.layout.StackPane

class NonSceneComponentFactory {

    val parent: MainUI

    constructor(parent: MainUI){
        this.parent = parent
    }

    fun waitingPage(): Scene {
        val root = GridPane()
        root.add(parent.generalComponents.makeBottomPane(listOf()), 0, 1)
        root.add(waitImage(), 0, 0)
        return Scene(root, parent.totalWidth, parent.totalHeight)
    }

    fun waitImage(): Pane {
        val imagePane = StackPane()
        val backgroundView = parent.generalComponents.makeImageView("assets//general//wait.png")
        imagePane.children.addAll(backgroundView)
        return imagePane
    }
}