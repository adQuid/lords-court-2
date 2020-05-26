package ui.componentfactory

import game.Writ
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import main.UIGlobals
import shortstate.ShortStateCharacter
import shortstate.room.action.DraftWrit
import ui.specialdisplayables.contructorobjects.WritConstructor

class WritComponentFactory {

    val writ: Writ


    constructor(parent: Writ){
        this.writ = parent
    }

    fun scenePage(perspective: ShortStateCharacter): Scene {
        val root = GridPane()

        root.add(UtilityComponentFactory.shortWideLabel(writ.name), 0,0)

        val description = UtilityComponentFactory.shortWideLabel(writ.deal.actions.map { entry -> entry.key.fullName()+" will "+entry.value.map{it.toString()}.joinToString() }.joinToString())
        root.add(description,0,1)

        root.add(UtilityComponentFactory.shortWideLabel("Signed by (${writ.prettyPrintSignatories()})"),0,2)

        root.add(UtilityComponentFactory.shortWideButton("Modify (clears signatories)", EventHandler { _ -> perspective.player.writs.remove(writ); UIGlobals.defocus(); UIGlobals.focusOn(WritConstructor(writ.deal.toUnfinishedDeal())) }), 0,3)
        val bottomPane = GridPane()
        bottomPane.add(UtilityComponentFactory.backButton(),0,4)

        root.add(bottomPane,0,5)

        val scene = Scene(root, UIGlobals.totalWidth(), UIGlobals.totalHeight())
        return scene
    }


}