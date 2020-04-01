package ui.contructorobjects

import game.GameCharacter
import javafx.scene.Scene
import main.UIGlobals
import shortstate.ShortStateCharacter
import ui.Displayable
import ui.specialdisplayables.selectionmodal.SelectionModal
import ui.specialdisplayables.selectionmodal.Tab

class CharacterSelector: Displayable {
    override fun display(perspective: ShortStateCharacter): Scene {
        val tabs = listOf(
            Tab<GameCharacter>(
                "Characters",
                UIGlobals.activeGame().players.toList()
            )
        )
        val selectModal = SelectionModal(tabs, { _ -> println("test") })
        return selectModal.getScene()
    }


}