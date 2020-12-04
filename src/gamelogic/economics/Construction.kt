package gamelogic.economics

import aibrain.UnfinishedDeal
import gamelogic.government.actionTypes.LaunchConstruction
import gamelogic.resources.Resources
import gamelogic.territory.mapobjects.Structure
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import main.UIGlobals
import shortstate.ShortStateCharacter
import ui.Describable
import ui.Displayable
import ui.commoncomponents.ResourceTransferWindow
import ui.componentfactory.UtilityComponentFactory
import javafx.event.EventHandler

class Construction: Describable, Displayable {
    
    val structure: Structure
    var budget: Resources
    val spentResources: Resources

    constructor(structure: Structure){
        this.structure = structure
        this.budget = Resources()
        this.spentResources = Resources()
    }

    constructor(structure: Structure, budget: Resources){
        this.structure = structure
        this.budget = budget
        this.spentResources = Resources()
    }

    constructor(other: Construction){
        this.structure = Structure(other.structure) //Structures are immutable, right?
        this.budget = Resources(other.budget)
        this.spentResources = Resources(other.spentResources)
    }

    constructor(saveString: Map<String, Any>){
        structure = Structure(saveString["structure"] as Map<String, Any>)
        budget = Resources(saveString["budget"] as Map<String, Any>)
        spentResources = Resources(saveString["spent"] as Map<String, Any>)
    }

    fun saveString(): Map<String, Any>{
        return mapOf(
            "structure" to structure.saveString(),
            "budget" to budget.saveString(),
            "spent" to spentResources.saveString()
        )
    }

    fun resourceNeeded(name: String): Int{
        return structure.type.cost.get(name) - spentResources.get(name)
    }

    fun complete(): Boolean{
        return structure.type.cost == spentResources
    }

    override fun tooltip(perspective: ShortStateCharacter): String {
        return "Constructing a ${structure.type.name}"
    }

    override fun description(): String {
        return "Constructing a ${structure.type.name}"
    }

    override fun universalDisplay(perspective: ShortStateCharacter?): Scene {
        val pane = GridPane()

        val fundingText = budget.resources.map { "${it.value} ${it.key}" }.joinToString(", ")
        val completionText = structure.type.cost.resources.map { "${spentResources.resources.getOrDefault(it.key, 0)}/${it.value} ${it.key}" }.joinToString(", ")

        pane.add(UtilityComponentFactory.shortWideLabel("Constructing a ${structure.type.name}"), 0,0)
        pane.add(UtilityComponentFactory.proportionalLabel("Funded by: \n$fundingText", 1.0, 0.3), 0,1)
        pane.add(UtilityComponentFactory.proportionalLabel("Completion: \n$completionText", 1.0, 0.3), 0,2)
        pane.add(
            UtilityComponentFactory.shortWideLabel("Modify Budget (coming soon)"),0, 3)
        pane.add(UtilityComponentFactory.shortButton("Abandon Construction", null),0,4)

        pane.add(UtilityComponentFactory.backButton(), 0, 5)

        return Scene(pane)
    }
}