package ui.componentfactory

import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import main.UIGlobals
import shortstate.ShortStateCharacter
import shortstate.room.action.DraftWrit
import ui.specialdisplayables.contructorobjects.WritConstructor

class WritConstructorComponentFactory {

    val parent: WritConstructor

    val textField = UtilityComponentFactory.shortWideTextField("Untitled Writ")

    constructor(parent: WritConstructor){
        this.parent = parent
    }

    fun scenePage(perspective: ShortStateCharacter): Scene {
        val root = GridPane()

        root.add(textField, 0,0)

        val description = UtilityComponentFactory.proportionalLabel(parent.deal.actions.filter{it.value.isNotEmpty()}.map { entry -> entry.key.fullName()+" will: "+entry.value.map{it.description()}.joinToString() }.joinToString("\n"), 1.0, 0.6)
        root.add(description,0,1)

        root.add(UtilityComponentFactory.shortWideButton("Modify Actions", EventHandler { _ -> UIGlobals.focusOn(parent.deal) }), 0,7)
        val bottomPane = GridPane()
        bottomPane.add(UtilityComponentFactory.shortButton("Cancel", EventHandler { UIGlobals.defocus()}, 1.5),0,0)
        if(parent.deal.isEmpty()){
            val emptyButton = UtilityComponentFactory.shortButton("(Writ is Empty)", EventHandler {}, 3)
            UtilityComponentFactory.setButtonDisable(emptyButton, true)
            bottomPane.add(emptyButton,1,0)
        } else {
            bottomPane.add(UtilityComponentFactory.proportionalButton(text = "Create Writ", action = EventHandler {finish(perspective)}, width = 3.0, sound = "writing.mp3"),1,0)
        }
        root.add(bottomPane,0,8)
        root.add(MiddlePaneComponentFactory.middlePane(perspective, true), 0,9)

        val scene = Scene(root, UIGlobals.totalWidth(), UIGlobals.totalHeight())
        return scene
    }

    private fun finish(perspective: ShortStateCharacter){
        parent.name = textField.text
        if(perspective.addEnergy(-DraftWrit(parent.deal, parent.name).cost())){
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