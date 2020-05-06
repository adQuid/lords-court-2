package ui.contructorobjects

import aibrain.UnfinishedDeal
import javafx.scene.Scene
import shortstate.ShortStateCharacter
import ui.PerspectiveDisplayable
import ui.componentfactory.WritConstructorComponentFactory

class WritConstructor: PerspectiveDisplayable {

    var name: String = "Untitled Writ"
    val deal: UnfinishedDeal

    val display = WritConstructorComponentFactory(this)

    constructor(deal: UnfinishedDeal): super(){
        this.deal = deal
    }

    override fun display(perspective: ShortStateCharacter): Scene {
        return display.scenePage(perspective)
    }


}