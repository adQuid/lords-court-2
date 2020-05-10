package ui.specialdisplayables

import game.gamelogicmodules.territory.TerritoryLogicModule
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import main.UIGlobals
import shortstate.ShortStateCharacter
import ui.PerspectiveDisplayable
import ui.componentfactory.UtilityComponentFactory

class EstatesView: PerspectiveDisplayable() {
    val pane = GridPane()

    override fun display(perspective: ShortStateCharacter): Scene {
        val gameCharacterSees = UIGlobals.activeGame().imageFor(perspective.player)
        val mapView = MapView((gameCharacterSees.moduleOfType(TerritoryLogicModule.type) as TerritoryLogicModule).map, 1.0, 1.0)

        pane.add(mapView.display(), 0,0)
        pane.add(UtilityComponentFactory.backButton(), 0, 1)

        return Scene(pane)
    }
}