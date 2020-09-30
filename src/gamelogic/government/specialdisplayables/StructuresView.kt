package gamelogic.government.specialdisplayables

import gamelogic.territory.TerritoryLogicModule
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import main.UIGlobals
import shortstate.ShortStateCharacter
import ui.Displayable
import ui.componentfactory.UtilityComponentFactory

class StructuresView: Displayable {
    val terId: Int

    constructor(terId: Int){
        this.terId = terId
    }

    override fun universalDisplay(perspective: ShortStateCharacter?): Scene {
        val territory = (UIGlobals.activeGame().moduleOfType(TerritoryLogicModule.type) as TerritoryLogicModule).territoryById(terId)

        val structures = territory.structures
        val pane = GridPane()

        pane.add(UtilityComponentFactory.proportionalLabel(structures.map{"${it.type.name}, owned by ${UIGlobals.activeGame().characterById(it.owner).fullName()}"}.joinToString("\n"), 1.0, 0.9), 0, 0)
        pane.add(UtilityComponentFactory.backButton(), 0, 9)

        return Scene(pane)
    }
}