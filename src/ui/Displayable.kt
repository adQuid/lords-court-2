package ui

import javafx.scene.Scene
import shortstate.ShortStateCharacter

interface Displayable {

    fun universalDisplay(perspective: ShortStateCharacter?): Scene
}