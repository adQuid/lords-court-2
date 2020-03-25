package ui

import aibrain.UnfinishedDeal
import javafx.scene.Scene
import shortstate.ShortStateCharacter
import ui.componentfactory.WritConstructorComponentFactory

class WritConstructor: Displayable {
    val deal: UnfinishedDeal

    val display = WritConstructorComponentFactory()

    constructor(deal: UnfinishedDeal){
        this.deal = deal
    }

    override fun display(perspective: ShortStateCharacter): Scene {
        return display.scenePage(perspective)
    }
}