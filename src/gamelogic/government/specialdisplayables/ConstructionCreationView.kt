package gamelogic.government.specialdisplayables

import gamelogic.economics.Construction
import gamelogic.territory.Territory
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import shortstate.ShortStateCharacter
import ui.Displayable
import ui.componentfactory.UtilityComponentFactory

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
        pane.add(UtilityComponentFactory.proportionalLabel("Funded by: \n$fundingText", 1.0, 0.4), 0,1)
        pane.add(UtilityComponentFactory.proportionalLabel("Completion: \n$completionText", 1.0, 0.3), 0,2)
        if(hostTerritory.constructions.contains(construction)){
            pane.add(UtilityComponentFactory.shortButton("Abandon Construction", null),0,3)
        } else {
            pane.add(UtilityComponentFactory.shortButton("Start Construction", null),0,3)
        }
        pane.add(UtilityComponentFactory.backButton(), 0, 4)

        return Scene(pane)
    }
}