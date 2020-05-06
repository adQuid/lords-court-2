package ui

import javafx.scene.Scene
import shortstate.ShortStateCharacter

abstract class PerspectiveDisplayable: Displayable {

    abstract fun display(perspective: ShortStateCharacter): Scene

    override fun universalDisplay(perspective: ShortStateCharacter?): Scene {
        return display(perspective!!)
    }

}