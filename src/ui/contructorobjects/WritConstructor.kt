package ui.contructorobjects

import aibrain.UnfinishedDeal
import game.GameCharacter
import game.Writ
import javafx.scene.Scene
import shortstate.GameRules
import shortstate.ShortStateCharacter
import ui.Displayable
import ui.componentfactory.WritConstructorComponentFactory

class WritConstructor: Displayable {

    var name: String = "Untitled Writ"
    val deal: UnfinishedDeal

    val display = WritConstructorComponentFactory(this)

    constructor(deal: UnfinishedDeal){
        this.deal = deal
    }

    override fun display(perspective: ShortStateCharacter): Scene {
        return display.scenePage(perspective)
    }


}