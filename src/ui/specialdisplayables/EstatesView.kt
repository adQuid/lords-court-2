package ui.specialdisplayables

import game.gamelogicmodules.capital.Count
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
        val territorysControlledByPlayer = (perspective.player.titles.filter { it is Count }.map{it as Count}).map{it.capital.territory!!}
        val primaryTerritory = territorysControlledByPlayer.first()

        val mapView = MapView((gameCharacterSees.moduleOfType(TerritoryLogicModule.type) as TerritoryLogicModule).map, 1.0, 0.9)
        mapView.focusX = primaryTerritory!!.x.toDouble()
        mapView.focusY = primaryTerritory!!.y.toDouble()
        mapView.zoom = 3.0

        pane.add(mapView.display(), 0,0)
        mapView.secondaryHighlight(territorysControlledByPlayer)
        pane.add(UtilityComponentFactory.backButton(), 0, 1)

        return Scene(pane)
    }
}