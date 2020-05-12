package ui.specialdisplayables

import game.gamelogicmodules.capital.Count
import game.gamelogicmodules.territory.Territory
import game.gamelogicmodules.territory.TerritoryLogicModule
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import main.UIGlobals
import shortstate.ShortStateCharacter
import ui.PerspectiveDisplayable
import ui.componentfactory.UtilityComponentFactory

class EstatesView: PerspectiveDisplayable {
    var focusedTerritory: Territory? = null

    val mapView: MapView

    constructor(perspective: ShortStateCharacter): super(){
        val gameCharacterSees = UIGlobals.activeGame().imageFor(perspective.player)
        val territorysControlledByPlayer = (perspective.player.titles.filter { it is Count }.map{it as Count}).map{it.capital.territory!!}
        val primaryTerritory = territorysControlledByPlayer.first()

        mapView = MapView((gameCharacterSees.moduleOfType(TerritoryLogicModule.type) as TerritoryLogicModule).map, 1.0, height())
        mapView.focusX = primaryTerritory!!.x.toDouble()
        mapView.focusY = primaryTerritory!!.y.toDouble()
        mapView.zoom = 3.0
        mapView.secondaryHighlight(territorysControlledByPlayer)
        mapView.onClick = {x,y -> focusedTerritory = mapView.selectTerritoryAt(x,y,false, false); mapView.resize(1.0, height()); UIGlobals.refresh()}
    }

    override fun display(perspective: ShortStateCharacter): Scene {
        val pane = GridPane()

        pane.add(mapView.display(), 0,0)

        if(focusedTerritory != null){
            mapView.selectTerritoryAt(focusedTerritory!!.x.toDouble(), focusedTerritory!!.y.toDouble(), true, false)

            val countTitleOfTerr = UIGlobals.activeGame().titles.filter { it is Count && it.capital.territory == focusedTerritory }.firstOrNull()
            val countOfTerr = UIGlobals.activeGame().players.filter { it.titles.contains(countTitleOfTerr) }.firstOrNull()
            val countText = if(countOfTerr == null){"no ruler"} else{ "Ruler: "+countOfTerr.fullName()}

            pane.add(UtilityComponentFactory.shortWideLabel(focusedTerritory!!.name), 0, 1)
            pane.add(UtilityComponentFactory.shortWideLabel(countText), 0, 2)
        }
        pane.add(UtilityComponentFactory.backButton(), 0, 3)

        return Scene(pane)
    }

    private fun height(): Double{
        if(focusedTerritory == null){
            return 0.9
        } else {
            return 0.7
        }
    }
}