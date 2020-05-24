package ui.componentfactory

import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import main.UIGlobals
import shortstate.ShortStateCharacter
import shortstate.room.action.DraftWrit
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
            bottomPane.add(UtilityComponentFactory.shortButton("Create Writ", EventHandler {finish(perspective)}, 3),1,0)
        }
        root.add(bottomPane,0,2)

        val scene = Scene(root, UIGlobals.totalWidth(), UIGlobals.totalHeight())
        return scene
    }

    private fun finish(perspective: ShortStateCharacter){
        parent.name = textField.text
        if(perspective.addEnergy(DraftWrit(parent.deal, parent.name).cost())){
            DraftWrit(parent.deal, parent.name).addWritToCharacter(perspective)
            UIGlobals.defocus()
            UIGlobals.defocus()
        } else {
            //TODO: send a warning that this failed
            UIGlobals.defocus()
            UIGlobals.defocus()
        }
    }
}