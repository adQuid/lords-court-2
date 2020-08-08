package gamelogic.government.specialdisplayables

import gamelogic.government.GovernmentLogicModule
import gamelogic.government.Count
import gamelogic.territory.Territory
import gamelogic.territory.TerritoryLogicModule
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import main.UIGlobals
import shortstate.ShortStateCharacter
import ui.PerspectiveDisplayable
import ui.componentfactory.UtilityComponentFactory
import ui.specialdisplayables.MapView

class TravelView: PerspectiveDisplayable {
    var focusedTerritory: Territory? = null

    val mapView: MapView

    constructor(perspective: ShortStateCharacter): super(){
        val gameCharacterSees = UIGlobals.activeGame().imageFor(perspective.player)
        val territoryLogic = (gameCharacterSees.moduleOfType(TerritoryLogicModule.type) as TerritoryLogicModule)
        val myLocation = perspective.player.location

        mapView = MapView(
            territoryLogic.map,
            1.0,
            height()
        )
        mapView.focusX = myLocation!!.x.toDouble()
        mapView.focusY = myLocation!!.y.toDouble()
        mapView.zoom = 3.0
        mapView.onClick = {x,y -> focusedTerritory = mapView.selectTerritoryAt(x,y, highlight = false, makeNew = false); mapView.resize(1.0, height()); UIGlobals.refresh()}
    }

    override fun display(perspective: ShortStateCharacter): Scene {
        val pane = GridPane()

        pane.add(mapView.display(), 0,0)
        mapView.secondaryHighlight(territoriesControlledByPlayer(perspective))

        if(focusedTerritory != null){
            mapView.selectTerritoryAt(focusedTerritory!!.x.toDouble(), focusedTerritory!!.y.toDouble(), true, false)

            val countOfTerr = (UIGlobals.activeGame().moduleOfType(GovernmentLogicModule.type) as GovernmentLogicModule).countOfCaptial((focusedTerritory!!.id))
            val countText = if(countOfTerr == null){""} else{ ", "+countOfTerr.fullName()}

            pane.add(UtilityComponentFactory.shortWideLabel(focusedTerritory!!.name + countText), 0, 1)

            if(countOfTerr == perspective.player){
                pane.add(UtilityComponentFactory.shortWideLabel("FILLER"), 0,2)
            } else {
                pane.add(UtilityComponentFactory.shortWideButton("Travel to Capital", EventHandler {  }), 0, 2)
            }
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

    private fun territoriesControlledByPlayer(perspective: ShortStateCharacter): Collection<Territory>{
        return (perspective.player.titles.filter { it is Count }.map{it as Count}).map{it.capital.territory!!}
    }

}