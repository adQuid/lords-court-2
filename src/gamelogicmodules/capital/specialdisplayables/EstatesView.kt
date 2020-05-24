package gamelogicmodules.capital.specialdisplayables

import gamelogicmodules.capital.CapitalLogicModule
import gamelogicmodules.capital.Count
import gamelogicmodules.territory.Territory
import gamelogicmodules.territory.TerritoryLogicModule
import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import main.Controller
import main.UIGlobals
import shortstate.ShortStateCharacter
import ui.PerspectiveDisplayable
import ui.componentfactory.UtilityComponentFactory
import ui.specialdisplayables.MapView
import ui.specialdisplayables.selectionmodal.SelectionModal
import ui.specialdisplayables.selectionmodal.Tab

class EstatesView: PerspectiveDisplayable {
    var focusedTerritory: Territory? = null

    val mapView: MapView

    constructor(perspective: ShortStateCharacter): super(){
        val gameCharacterSees = UIGlobals.activeGame().imageFor(perspective.player)
        val territorysControlledByPlayer = territoriesControlledByPlayer(perspective)
        val primaryTerritory = territorysControlledByPlayer.first()

        mapView = MapView(
            (gameCharacterSees.moduleOfType(TerritoryLogicModule.type) as TerritoryLogicModule).map,
            1.0,
            height()
        )
        mapView.focusX = primaryTerritory!!.x.toDouble()
        mapView.focusY = primaryTerritory!!.y.toDouble()
        mapView.zoom = 3.0
        mapView.secondaryHighlight(territorysControlledByPlayer)
        mapView.onClick = {x,y -> focusedTerritory = mapView.selectTerritoryAt(x,y, highlight = false, makeNew = false); mapView.resize(1.0, height()); UIGlobals.refresh()}
    }

    override fun display(perspective: ShortStateCharacter): Scene {
        val pane = GridPane()

        pane.add(mapView.display(), 0,0)
        mapView.secondaryHighlight(territoriesControlledByPlayer(perspective))

        if(focusedTerritory != null){
            mapView.selectTerritoryAt(focusedTerritory!!.x.toDouble(), focusedTerritory!!.y.toDouble(), true, false)

            val countTitleOfTerr = UIGlobals.activeGame().titles.filter { it is Count && it.capital.territory == focusedTerritory }.firstOrNull()
            val countOfTerr = UIGlobals.activeGame().players.filter { it.titles.contains(countTitleOfTerr) }.firstOrNull()
            val countText = if(countOfTerr == null){"no ruler"} else{ "Ruler: "+countOfTerr.fullName()}

            pane.add(UtilityComponentFactory.shortWideLabel(focusedTerritory!!.name), 0, 1)

            if(countOfTerr == perspective.player){
                pane.add(actionsOnMyTerritory(perspective, countTitleOfTerr as Count), 0,2)
            } else {
                pane.add(UtilityComponentFactory.shortWideLabel(countText), 0, 2)
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

    private fun actionsOnMyTerritory(perspective: ShortStateCharacter, countTitle: Count): Node {
        val logic = UIGlobals.activeGame().moduleOfType(CapitalLogicModule.type) as CapitalLogicModule
        val buttonsPane = GridPane()
        buttonsPane.add(UtilityComponentFactory.proportionalButton("Reports", EventHandler { UIGlobals.focusOn(
            SelectionModal("Select Action",
                listOf(Tab("Reports", countTitle!!.reportActions())),
                { maker ->
                    maker.onClick(Controller.singleton!!.shortThreadForPlayer(perspective).shortGame,perspective)
                })
        ) }, 2.0),0,0)
        buttonsPane.add(UtilityComponentFactory.proportionalButton("Laws", EventHandler {UIGlobals.focusOn(
            SelectionModal("Select Action",
                listOf(Tab("Laws", logic.legalActionsReguarding(perspective.player, countTitle.capital))),
                { maker ->
                    maker.onClick(Controller.singleton!!.shortThreadForPlayer(perspective).shortGame,perspective)
                })
        ) }, 2.0),1,0)
        return buttonsPane
    }
}