package ui.componentfactory

import javafx.scene.Scene
import javafx.scene.layout.GridPane
import shortstate.ShortStateCharacter
import ui.totalHeight
import ui.totalWidth

class WritConstructorComponentFactory {

    fun scenePage(perspective: ShortStateCharacter): Scene {
        val root = GridPane()

        root.add(UtilityComponentFactory.backButton(), 0,0)

        val scene = Scene(root, totalWidth, totalHeight)
        return scene
    }
}