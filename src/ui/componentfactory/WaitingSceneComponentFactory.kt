package ui.componentfactory

import javafx.scene.Scene
import javafx.scene.layout.GridPane
import javafx.scene.layout.Pane
import javafx.scene.layout.StackPane
import ui.MainUI

class WaitingSceneComponentFactory {

    val parent: MainUI

    constructor(parent: MainUI){
        this.parent = parent
    }

    fun waitingPage(): Scene {
        val root = GridPane()
        root.add(parent.utilityComponents.bottomPane(listOf()), 0, 1)
        root.add(waitImage(), 0, 0)
        return Scene(root, parent.totalWidth, parent.totalHeight)
    }

    private fun waitImage(): Pane {
        val imagePane = StackPane()
        val backgroundView = parent.utilityComponents.imageView("assets//general//wait.png")
        imagePane.children.addAll(backgroundView)
        return imagePane
    }
}