package ui.specialdisplayables

import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.layout.Pane
import main.UIGlobals
import shortstate.ShortStateCharacter
import shortstate.ShortStateGame
import ui.NoPerspectiveDisplayable
import ui.componentfactory.UtilityComponentFactory

class Message: NoPerspectiveDisplayable {

    val text: String
    val onClose: (game: ShortStateGame, player: ShortStateCharacter) -> Unit

    constructor(text: String): this(text, { shortStateGame: ShortStateGame, shortStateCharacter: ShortStateCharacter -> })

    constructor(text: String, onClose: (game: ShortStateGame, player: ShortStateCharacter) -> Unit){
        this.text = text
        this.onClose = onClose
    }

    override fun display(): Scene {
        val pane = Pane()

        val label = UtilityComponentFactory.proportionalLabel(text, 1.0, 1.0)

        label.onMouseClicked = EventHandler { UIGlobals.defocus(); onClose(UIGlobals.activeShortGame(), UIGlobals.playingAs()) }
        pane.children.add(label)
        return Scene(pane)
    }
}