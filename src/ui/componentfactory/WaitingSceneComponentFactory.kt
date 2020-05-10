package ui.componentfactory

import javafx.scene.Scene
import javafx.scene.layout.GridPane
import javafx.scene.layout.Pane
import javafx.scene.layout.StackPane
import main.UIGlobals
import shortstate.ShortStateCharacter
import ui.MainUI

class WaitingSceneComponentFactory {

    fun waitingPage(perspective: ShortStateCharacter): Scene {
        val root = GridPane()
        root.add(BottomPaneComponentFactory.bottomPane(listOf(), perspective), 0, 1)
        root.add(waitImage(), 0, 0)
        return Scene(root, UIGlobals.totalWidth(), UIGlobals.totalHeight())
    }

    private fun waitImage(): Pane {
        val imagePane = StackPane()
        val backgroundView = UtilityComponentFactory.imageView("assets/general/wait.png", 0.8)
        imagePane.children.addAll(backgroundView)
        return imagePane
    }
}