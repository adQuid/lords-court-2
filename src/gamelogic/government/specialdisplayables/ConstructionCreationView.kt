package gamelogic.government.specialdisplayables

import aibrain.UnfinishedDeal
import gamelogic.economics.Construction
import gamelogic.government.actionTypes.LaunchConstruction
import gamelogic.territory.Territory
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import main.UIGlobals
import shortstate.ShortStateCharacter
import ui.Displayable
import ui.commoncomponents.ResourceTransferWindow
import ui.componentfactory.UtilityComponentFactory
import ui.specialdisplayables.contructorobjects.WritConstructor

class ConstructionCreationView: Displayable {
    val construction: Construction
    val hostTerritory: Territory

    constructor(construction: Construction, territory: Territory){
        this.construction = construction
        hostTerritory = territory
    }

    override fun universalDisplay(perspective: ShortStateCharacter?): Scene {
        val pane = GridPane()

        val fundingText = construction.budget.resources.map { "${it.value} ${it.key}" }.joinToString(", ")
        val completionText = construction.structure.type.cost.resources.map { "${construction.budget.resources.getOrDefault(it.key, 0)}/${it.value} ${it.key}" }.joinToString(", ")

        pane.add(UtilityComponentFactory.shortWideLabel("Constructing a ${construction.structure.type.name}"), 0,0)
        pane.add(UtilityComponentFactory.proportionalLabel("Funded by: \n$fundingText", 1.0, 0.3), 0,1)
        pane.add(UtilityComponentFactory.proportionalLabel("Completion: \n$completionText", 1.0, 0.3), 0,2)
        pane.add(UtilityComponentFactory.shortButton("Modify budget",
            EventHandler{UIGlobals.focusOn(ResourceTransferWindow(UIGlobals.activeGame().resourcesByCharacter(perspective!!.player).minus(construction.budget), construction.budget,
                {res1, res2 -> construction.budget = res2}))}),0, 3)
        if(hostTerritory.constructions.contains(construction)){
            pane.add(UtilityComponentFactory.shortButton("Abandon Construction", null),0,4)
        } else {
            pane.add(UtilityComponentFactory.shortButton("Start Construction", EventHandler
            {
                UIGlobals.GUI().curFocus.pop()
                UIGlobals.GUI().curFocus.pop()
                UIGlobals.focusOn(WritConstructor(UnfinishedDeal(mapOf(UIGlobals.playingAs().player to setOf(LaunchConstruction(this.construction, this.hostTerritory.id))))))
            }),0,4)
        }
        pane.add(UtilityComponentFactory.backButton(), 0, 5)

        return Scene(pane)
    }
}