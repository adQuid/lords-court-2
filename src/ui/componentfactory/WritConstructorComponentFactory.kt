package ui.componentfactory

import game.Writ
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import main.Controller
import shortstate.ShortStateCharacter
import ui.WritConstructor
import ui.totalHeight
import ui.totalWidth

class WritConstructorComponentFactory {

    val parent: WritConstructor

    constructor(parent: WritConstructor){
        this.parent = parent
    }

    fun scenePage(perspective: ShortStateCharacter): Scene {
        val root = GridPane()

        root.add(UtilityComponentFactory.shortWideButton("Modify Deal Directly", EventHandler { _ -> Controller.singleton!!.GUI!!.focusOn(parent.deal) }), 0,0)
        val bottomPane = GridPane()
        bottomPane.add(UtilityComponentFactory.shortButton("Cancel", EventHandler { Controller.singleton!!.GUI!!.defocus()}),0,0)
        bottomPane.add(UtilityComponentFactory.shortButton("Create Writ", EventHandler { perspective.player.writs.add(parent.generateWrit()); Controller.singleton!!.GUI!!.defocus()}, 3),1,0)
        root.add(bottomPane,0,1)

        val scene = Scene(root, totalWidth, totalHeight)
        return scene
    }
}