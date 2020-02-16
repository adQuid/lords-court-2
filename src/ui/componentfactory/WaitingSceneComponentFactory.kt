package ui.componentfactory

import javafx.scene.Scene
import javafx.scene.layout.GridPane
import javafx.scene.layout.Pane
import javafx.scene.layout.StackPane
import shortstate.ShortStateCharacter
import ui.MainUI

class WaitingSceneComponentFactory {

    val parent: MainUI

    constructor(parent: MainUI){
        this.parent = parent
    }

    fun waitingPage(perspective: ShortStateCharacter): Scene {
        val root = GridPane()
        root.add(UtilityComponentFactory.bottomPane(listOf(), perspective), 0, 1)
        root.add(waitImage(), 0, 0)
        return Scene(root, parent.totalWidth, parent.totalHeight)
    }

    private fun waitImage(): Pane {
        val imagePane = StackPane()
        val backgroundView = UtilityComponentFactory.imageView("assets//general//wait.png")
        imagePane.children.addAll(backgroundView)
        return imagePane
    }
}