package gamelogic.government.specialdisplayables

import gamelogic.economics.Construction
import gamelogic.territory.TerritoryLogicModule
import gamelogic.territory.mapobjects.Structure
import gamelogic.territory.mapobjects.StructureType
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import main.UIGlobals
import shortstate.ShortStateCharacter
import ui.Displayable
import ui.componentfactory.UtilityComponentFactory
import ui.specialdisplayables.selectionmodal.SelectionModal
import ui.specialdisplayables.selectionmodal.Tab

class StructuresView: Displayable {
    val terId: Int

    constructor(terId: Int){
        this.terId = terId
    }

    override fun universalDisplay(perspective: ShortStateCharacter?): Scene {
        val territory = (UIGlobals.activeGame().moduleOfType(TerritoryLogicModule.type) as TerritoryLogicModule).territoryById(terId)

        val structures = territory.structures
        val pane = GridPane()

        pane.add(UtilityComponentFactory.proportionalLabel(structures.map{"${it.type.name}, owned by ${nameOrYou(perspective, it.owner)}"}.joinToString("\n"), 1.0, 0.7), 0, 0)
        pane.add(UtilityComponentFactory.shortButton("Construction sites", EventHandler { UIGlobals.focusOn(SelectionModal("Construction", listOf(
            Tab("constructions", territory.constructions)), null
        ) )}), 0, 7)
        pane.add(UtilityComponentFactory.shortButton("Start new Construction", EventHandler {UIGlobals.focusOn(SelectionModal("Building Type", listOf(
            Tab("Building Types", StructureType.allTypes)), { type -> UIGlobals.focusOn(ConstructionCreationView(
            Construction(
            Structure(UIGlobals.playingAs().player.id, type)
        ), territory
        ))}
        ))}), 0 ,8)
        pane.add(UtilityComponentFactory.backButton(), 0, 9)

        return Scene(pane)
    }

    private fun nameOrYou(perspective: ShortStateCharacter?, ownerId: Int): String{
        if(perspective == null || perspective!!.player.id != ownerId){
            return UIGlobals.activeGame().characterById(ownerId).fullName()
        } else {
            return "you"
        }
    }
}