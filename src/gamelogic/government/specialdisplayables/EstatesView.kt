package gamelogic.government.specialdisplayables

import gamelogic.government.GovernmentLogicModule
import gamelogic.government.Count
import gamelogic.territory.Territory
import gamelogic.territory.TerritoryLogicModule
import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import main.Controller
import main.UIGlobals
import shortstate.ShortStateCharacter
import ui.PerspectiveDisplayable
import ui.componentfactory.MiddlePaneComponentFactory
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

        val kingdom = (UIGlobals.activeGame().moduleOfType(GovernmentLogicModule.type) as GovernmentLogicModule).kingdomOf(territoriesControlledByPlayer(perspective).first())
        if(kingdom != null){
            mapView.tertiaryHighlight(kingdom.territories!!)
        }

        if(focusedTerritory != null){
            mapView.selectTerritoryAt(focusedTerritory!!.x.toDouble(), focusedTerritory!!.y.toDouble(), true, false)

            val govLogic = (UIGlobals.activeGame().moduleOfType(GovernmentLogicModule.type) as GovernmentLogicModule)

            val countTitleOfTerr = UIGlobals.activeGame().titles.filter { it is Count && it.capital.territory == focusedTerritory }.firstOrNull()
            val countOfTerr = govLogic.countOfCaptial((focusedTerritory!!.id))
            val countText = if(countOfTerr == null){"no ruler"} else{ "Ruler: "+countOfTerr.fullName()}
            val kingdom = govLogic.kingdomOf(focusedTerritory!!)
            val kingdomText = if(kingdom != null){ "Part of ${kingdom.name}, ruled by ${govLogic.kingOfKingdom(kingdom.name)}" } else { "Independent" }

            pane.add(UtilityComponentFactory.shortWideLabel(focusedTerritory!!.name + "   (Arable land: ${focusedTerritory!!.resources.get(Territory.ARABLE_LAND_NAME)})"), 0, 1)

            if(countOfTerr == perspective.player){
                pane.add(actionsOnMyTerritory(perspective, countTitleOfTerr as Count), 0,2)
            } else {
                val ownerPanel = GridPane()
                ownerPanel.add(UtilityComponentFactory.shortProportionalLabel(countText, 2.0), 0, 0)
                ownerPanel.add(UtilityComponentFactory.shortProportionalLabel(kingdomText, 2.0), 1, 0)
                pane.add(ownerPanel, 0, 2)
            }
        }
        pane.add(UtilityComponentFactory.backButton(), 0, 3)
        pane.add(MiddlePaneComponentFactory.middlePane(perspective, true),0,4)

        return Scene(pane)
    }

    private fun height(): Double{
        if(focusedTerritory == null){
            return 0.8
        } else {
            return 0.6
        }
    }

    private fun territoriesControlledByPlayer(perspective: ShortStateCharacter): Collection<Territory>{
        return (perspective.player.titles.filter { it is Count }.map{it as Count}).map{it.capital.territory!!}
    }

    private fun actionsOnMyTerritory(perspective: ShortStateCharacter, countTitle: Count): Node {
        val buttonsPane = GridPane()
        buttonsPane.add(UtilityComponentFactory.proportionalButton("Reports", EventHandler { UIGlobals.focusOn(
            SelectionModal("Select Action",
                listOf(Tab("Reports", countTitle!!.reportsIDontAlreadyHave(perspective))),
                { maker ->
                    maker.onClick(Controller.singleton!!.shortThreadForPlayer(perspective).shortGame,perspective)
                })
        ) }, 2.0),0,0)
        buttonsPane.add(UtilityComponentFactory.proportionalButton("Laws", EventHandler {UIGlobals.focusOn(
            LawsView(countTitle.capital.terId)
        ) }, 2.0),1,0)
        return buttonsPane
    }
}