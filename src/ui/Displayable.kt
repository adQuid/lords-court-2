package ui

import javafx.scene.Scene
import shortstate.ShortStateCharacter

interface Displayable {

    fun display(perspective: ShortStateCharacter): Scene

}