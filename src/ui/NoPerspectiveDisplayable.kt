package ui

import javafx.scene.Scene
import shortstate.ShortStateCharacter

abstract class NoPerspectiveDisplayable: Displayable {

    abstract fun display(): Scene

    override fun universalDisplay(perspective: ShortStateCharacter?): Scene {
        return display()
    }

}