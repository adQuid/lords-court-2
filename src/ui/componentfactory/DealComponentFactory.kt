package ui.componentfactory

import aibrain.Deal
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.layout.GridPane
import main.Controller
import shortstate.ShortStateCharacter
import ui.totalHeight
import ui.totalWidth

class DealComponentFactory {

    val deal: Deal

    constructor(deal: Deal){
        this.deal = deal
    }

    fun scenePage(perspective: ShortStateCharacter): Scene {
        val root = GridPane()

        root.add(UtilityComponentFactory.shortWideButton("Back", EventHandler { Controller.singleton!!.GUI!!.defocus()}),0,0)

        val scene = Scene(root, totalWidth, totalHeight)
        return scene
    }

}