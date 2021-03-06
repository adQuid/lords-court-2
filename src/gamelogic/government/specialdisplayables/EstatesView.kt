package gamelogic.government.specialdisplayables

import gamelogic.government.GovernmentLogicModule
import gamelogic.government.Count
import gamelogic.resources.ResourceTypes
import gamelogic.territory.Territory
import gamelogic.territory.TerritoryLogicModule
import gamelogic.territory.mapobjects.Fleet
import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.Label
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
    var focusedTerritory: Territory

    val mapView: MapView

    var cachedScene: Scene? = null
    var cachedFocusedTerritory: Territory? = null

    val pane = GridPane()
    val territoryNameLabel = UtilityComponentFactory.shortWideLabel("")
    val rulerLabel = UtilityComponentFactory.shortProportionalLabel("", 0.5)
    val kingdomLabel = UtilityComponentFactory.shortProportionalLabel("", 0.5)

    val actionsHerePane = GridPane()

    constructor(perspective: ShortStateCharacter): super(){
        val gameCharacterSees = UIGlobals.activeGame().imageFor(perspective.player)
        val territorysControlledByPlayer = territoriesControlledByPlayer(perspective)
        val primaryTerritory = territorysControlledByPlayer.first()
        focusedTerritory = primaryTerritory

        mapView = MapView(
            (gameCharacterSees.moduleOfType(TerritoryLogicModule.type) as TerritoryLogicModule).map,
            1.0,
            height()
        )
        mapView.focusX = primaryTerritory!!.x.toDouble()
        mapView.focusY = primaryTerritory!!.y.toDouble()
        mapView.zoom = 3.0
        mapView.onClick =
            {x,y -> selectTerritory(x,y)}
    }

    private fun selectTerritory(x:Double, y:Double){
        val oldHeight = height()
        val selectedTerritory = mapView.selectTerritoryAt(x,y, highlight = false, makeNew = false)
        if(selectedTerritory != null){
            focusedTerritory = selectedTerritory
        }
        if(height() != oldHeight){
            mapView.resize(1.0, height())
        }
        UIGlobals.refresh()
    }

    override fun display(perspective: ShortStateCharacter): Scene {
        if(cachedScene == null){
            setupScene(perspective)
        } else {
            updateScene(perspective)
        }
        cachedFocusedTerritory = focusedTerritory
        return cachedScene!!
    }

    private fun setupScene(perspective: ShortStateCharacter){

        pane.add(mapView.display(), 0,0)

        highlightAreas(perspective)

        val govLogic = (UIGlobals.activeGame().moduleOfType(GovernmentLogicModule.type) as GovernmentLogicModule)

        val kingdom = (UIGlobals.activeGame().moduleOfType(GovernmentLogicModule.type) as GovernmentLogicModule).kingdomOf(territoriesControlledByPlayer(perspective).first())
        val countTitleOfTerr = UIGlobals.activeGame().titles.filter { it is Count && it.capital.territory == focusedTerritory }.firstOrNull()
        val countOfTerr = govLogic.countOfCaptial((focusedTerritory!!.id))
        val countText = if(countOfTerr == null){"no ruler"} else{ "Ruler: "+countOfTerr.fullName()}
        val kingdomText = if(kingdom != null){ "Part of ${kingdom.name}, ruled by ${govLogic.kingOfKingdom(kingdom.name)}" } else { "Independent" }

        (territoryNameLabel.children[1] as Label).text = focusedTerritory!!.name + "   (Arable land: ${focusedTerritory!!.resources.get(
            ResourceTypes.ARABLE_LAND_NAME)})"
        (rulerLabel.children[1] as Label).text = countText
        (kingdomLabel.children[1] as Label).text = kingdomText
        pane.add(territoryNameLabel, 0, 1)

        if(countOfTerr == perspective.player){
            pane.add(actionsOnMyTerritory(perspective, countTitleOfTerr as Count), 0,2)
        } else {
            val ownerPanel = GridPane()
            ownerPanel.add(rulerLabel, 0, 0)
            ownerPanel.add(kingdomLabel, 1, 0)
            pane.add(ownerPanel, 0, 2)
        }

        pane.add(UtilityComponentFactory.backButton(), 0, 3)
        pane.add(MiddlePaneComponentFactory.middlePane(perspective, true),0,4)

        cachedScene = Scene(pane)
    }

    private fun updateScene(perspective: ShortStateCharacter){
        mapView.refresh()
        highlightAreas(perspective)
        (territoryNameLabel.children[1] as Label).text = focusedTerritory!!.name + "   (Arable land: ${focusedTerritory!!.resources.get(
            ResourceTypes.ARABLE_LAND_NAME)})"

        val govLogic = (UIGlobals.activeGame().moduleOfType(GovernmentLogicModule.type) as GovernmentLogicModule)
        val countOfTerr = govLogic.countOfCaptial((focusedTerritory!!.id))
        val countTitleOfTerr = UIGlobals.activeGame().titles.filter { it is Count && it.capital.territory == focusedTerritory }.firstOrNull()
        if(countOfTerr == perspective.player){
            pane.add(actionsOnMyTerritory(perspective, countTitleOfTerr as Count), 0,2)
        } else {
            val govLogic = (UIGlobals.activeGame().moduleOfType(GovernmentLogicModule.type) as GovernmentLogicModule)

            val kingdom = (UIGlobals.activeGame().moduleOfType(GovernmentLogicModule.type) as GovernmentLogicModule).kingdomOf(focusedTerritory)
            val countOfTerr = govLogic.countOfCaptial((focusedTerritory!!.id))
            val countText = if(countOfTerr == null){"no ruler"} else{ "Ruler: "+countOfTerr.fullName()}
            val kingdomText = if(kingdom != null){ "Part of ${kingdom.name}, ruled by ${govLogic.kingOfKingdom(kingdom.name)}" } else { "Independent" }

            (rulerLabel.children[1] as Label).text = countText
            (kingdomLabel.children[1] as Label).text = kingdomText

            val ownerPanel = GridPane()
            ownerPanel.add(rulerLabel, 0, 0)
            ownerPanel.add(kingdomLabel, 1, 0)
            pane.add(ownerPanel, 0, 2)
        }
    }

    private fun highlightAreas(perspective: ShortStateCharacter){
        mapView.selectTerritoryAt(focusedTerritory!!.x.toDouble(), focusedTerritory!!.y.toDouble(), true, false)

        mapView.secondaryHighlight(territoriesControlledByPlayer(perspective))

        val kingdom = (UIGlobals.activeGame().moduleOfType(GovernmentLogicModule.type) as GovernmentLogicModule).kingdomOf(territoriesControlledByPlayer(perspective).first())
        if(kingdom != null){
            mapView.tertiaryHighlight(kingdom.territories!!)
        }
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

        val fleetsTab = Tab("Fleets Spotted", focusedTerritory.fleets)

        buttonsPane.add(UtilityComponentFactory.proportionalButton("Units (${focusedTerritory!!.fleets.size})", EventHandler{UIGlobals.focusOn(SelectionModal("Units here", listOf(fleetsTab), {}))}, 0.25), 0,0)
        buttonsPane.add(UtilityComponentFactory.proportionalButton("Structures (${focusedTerritory!!.structures.size})", EventHandler { UIGlobals.focusOn(StructuresView(focusedTerritory!!.id))}, 0.25), 1,0)
        buttonsPane.add(UtilityComponentFactory.proportionalButton("Reports", EventHandler { UIGlobals.focusOn(
            SelectionModal("Select Report",
                listOf(Tab("Reports", countTitle!!.reportsIDontAlreadyHave(perspective, UIGlobals.activeGame()))),
                { maker ->
                    maker.onClick(Controller.singleton!!.shortThreadForShortPlayer(perspective).shortGame,perspective)
                })
        ) }, 0.25),2,0)
        buttonsPane.add(UtilityComponentFactory.proportionalButton("Laws", EventHandler {UIGlobals.focusOn(
            LawsView(countTitle.capital.terId)
        ) }, 0.25),3,0)
        return buttonsPane
    }
}