package ui.componentfactory

import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import main.UIGlobals
import shortstate.ShortStateCharacter
import ui.contructorobjects.WritConstructor

class WritConstructorComponentFactory {

    val parent: WritConstructor

    val textField = UtilityComponentFactory.shortWideTextField("Untitled Writ")

    constructor(parent: WritConstructor){
        this.parent = parent
    }

    fun scenePage(perspective: ShortStateCharacter): Scene {
        val root = GridPane()

        root.add(textField, 0,0)
        root.add(UtilityComponentFactory.shortWideButton("Modify Deal Directly", EventHandler { _ -> UIGlobals.focusOn(parent.deal) }), 0,1)
        val bottomPane = GridPane()
        bottomPane.add(UtilityComponentFactory.shortButton("Cancel", EventHandler { UIGlobals.defocus()}),0,0)
        if(parent.deal.isEmpty()){
            bottomPane.add(UtilityComponentFactory.shortButton("(Writ is Empty)", EventHandler {}, 3),1,0)
        } else {
            bottomPane.add(UtilityComponentFactory.shortButton("Create Writ", EventHandler { parent.name = textField.text; perspective.player.writs.add(parent.generateWrit(perspective.player)); UIGlobals.defocus()}, 3),1,0)
        }
        root.add(bottomPane,0,2)

        val scene = Scene(root, UIGlobals.totalWidth(), UIGlobals.totalHeight())
        return scene
    }
}